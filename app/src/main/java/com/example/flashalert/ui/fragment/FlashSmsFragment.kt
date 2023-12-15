package com.example.flashalert.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flashalert.Broadcast.FlashLight
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentFlashSmsBinding
import com.example.flashalert.prefence.MyPreferences
import com.example.flashalert.viewmodel.SmsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FlashSmsFragment : Fragment() {

    private var _binding: FragmentFlashSmsBinding? = null
    private val binding get() = _binding

    val viewmodel=SmsViewModel()
    private val flashlight = FlashLight()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFlashSmsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.onDelay.observe(viewLifecycleOwner){value ->
            binding?.tvSmsOnDelay?.text = value.toString()

        }

        viewmodel.offDelay.observe(viewLifecycleOwner){value ->
            binding?.tvOffSmsDelay?.text = value.toString()
        }

        viewmodel.callSwitch.observe(viewLifecycleOwner){value ->
            binding?.swSms?.isChecked =value

        }

        val smsTimeOnSeekBarValue = MyPreferences.getInstance(requireContext()).prefSmsTimeOn
        binding?.seekbarSmsOnDelay?.progress = smsTimeOnSeekBarValue.toInt()
        binding?.tvSmsOnDelay?.text = smsTimeOnSeekBarValue.toString()

        binding?.seekbarSmsOnDelay?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewmodel.onDelay.value = progress
                MyPreferences.getInstance(requireContext()).putSeekBarValue(MyPreferences.SMS_TIME_ON, progress.toLong())
                // TextView'ı güncelleme
                binding?.tvSmsOnDelay?.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        val smsTimeOffSeekBarValue = MyPreferences.getInstance(requireContext()).prefSmsTimeOff
        binding?.seekbarSmsOffDelay?.progress = smsTimeOffSeekBarValue.toInt()
        binding?.tvOffSmsDelay?.text = smsTimeOffSeekBarValue.toString()

        binding?.seekbarSmsOffDelay?.setOnSeekBarChangeListener(object :OnSeekBarChangeListener{

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               viewmodel.offDelay.value = progress
                MyPreferences.getInstance(requireContext()).putSeekBarValue(MyPreferences.SMS_TIME_OFF, progress.toLong())
                // TextView'ı güncelleme
                binding?.tvOffSmsDelay?.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding?.swSms?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewmodel.checkPermissions(isChecked,requireActivity())
        }

        binding?.btnFlashSmsTest?.setOnClickListener {
            flashlight.isFlashOn = !flashlight.isFlashOn
            var flashJob: Job? = null // Coroutine job değişkeni
            val scope = CoroutineScope(Dispatchers.IO)
            scope.launch {

            }
            if (flashlight.isFlashOn) {
                // Coroutine başlat
                lifecycleScope.launch {
                    // Sonsuz döngü
                    while (flashlight.isFlashOn) {
                        // Seekbar değerlerini al
                        val testoffDelay = binding?.seekbarSmsOnDelay?.progress ?: 0
                        val testOnDelay = binding?.seekbarSmsOffDelay?.progress ?: 0
                        // Flashı aç
                        flashlight.flash(
                            requireContext(),false,
                            testOnDelay.toLong(),
                            testoffDelay.toLong(),
                            10
                        )
                        // On delay süresi kadar bekle
                        delay(testOnDelay.toLong())
                        // Flashı kapat
                        flashlight.flash(
                            requireContext(),false,
                            testOnDelay.toLong(),
                            testoffDelay.toLong(),
                            10
                        )
                        // Off delay süresi kadar bekle
                        delay(testoffDelay.toLong())
                    }
                }
                binding?.btnFlashSmsTest?.text = "Stop"
            } else {
                // Coroutine durdur
                flashJob?.cancel()
                flashlight.flash( requireContext(),false, 0, 0, 10)
                binding?.btnFlashSmsTest?.text = "Start"
            }
        }


        val toolbar = binding?.toolbar
        val navController = findNavController()
        toolbar?.setupWithNavController(navController)
        toolbar?.setTitle("Flash on SMS")
        toolbar?.setNavigationOnClickListener {
            navController.navigate(R.id.smsToHome)


        }

    }

}