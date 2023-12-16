package com.example.flashalert.ui.fragment

import com.example.flashalert.Broadcast.FlashLight
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
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentFlashSmsBinding
import com.example.flashalert.prefence.MyPreferences
import com.example.flashalert.viewmodel.SmsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FlashSmsFragment : Fragment() {

    private var _binding: FragmentFlashSmsBinding? = null
    private val binding get() = _binding

    val viewmodel = SmsViewModel()
    private val flashlight = FlashLight()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFlashSmsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.onDelay.observe(viewLifecycleOwner) { value ->
            binding?.tvSmsOnDelay?.text = value.toString()

        }

        viewmodel.offDelay.observe(viewLifecycleOwner) { value ->
            binding?.tvOffSmsDelay?.text = value.toString()
        }

        viewmodel.callSwitch.observe(viewLifecycleOwner) { value ->
            binding?.swSms?.isChecked = value

        }

        val smsTimeOnSeekBarValue = MyPreferences.getInstance(requireContext()).prefSmsTimeOn
        binding?.seekbarSmsOnDelay?.progress = smsTimeOnSeekBarValue.toInt()
        binding?.tvSmsOnDelay?.text = smsTimeOnSeekBarValue.toString()

        binding?.seekbarSmsOnDelay?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewmodel.onDelay.value = progress
                MyPreferences.getInstance(requireContext())
                    .putSeekBarValue(MyPreferences.SMS_TIME_ON, progress.toLong())
                // TextView'ı güncelleme
                binding?.tvSmsOnDelay?.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        val smsTimeOffSeekBarValue = MyPreferences.getInstance(requireContext()).prefSmsTimeOff
        binding?.seekbarSmsOffDelay?.progress = smsTimeOffSeekBarValue.toInt()
        binding?.tvOffSmsDelay?.text = smsTimeOffSeekBarValue.toString()

        binding?.seekbarSmsOffDelay?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewmodel.offDelay.value = progress
                MyPreferences.getInstance(requireContext())
                    .putSeekBarValue(MyPreferences.SMS_TIME_OFF, progress.toLong())
                // TextView'ı güncelleme
                binding?.tvOffSmsDelay?.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding?.swSms?.setOnCheckedChangeListener { buttonView, isChecked ->
            viewmodel.checkPermissions(isChecked, requireActivity())
        }

        binding?.btnFlashSmsTest?.setOnClickListener {
            flashlight.isFlashOn = !flashlight.isFlashOn

            if (flashlight.isFlashOn) {
                // Coroutine başlat
                val testOffDelay = binding?.seekbarSmsOnDelay?.progress?.toLong() ?: 0
                val testOnDelay = binding?.seekbarSmsOffDelay?.progress?.toLong() ?: 0

                lifecycleScope.launch {
                    // Sonsuz döngü
                    while (flashlight.isFlashOn) {
                        // Flash'ı aç
                        flashlight.flash(
                            context,
                            onDelay = testOnDelay,
                            offDelay = testOffDelay,
                            count = 10
                        )
                        // On delay süresi kadar bekle
                        delay(testOnDelay)
                        // Flash'ı kapat
                        // Off delay süresi kadar bekle
                        delay(testOffDelay)
                    }
                }
                binding?.btnFlashSmsTest?.text = "Stop"
            } else {
                // Coroutine durdur
                flashlight.isFlashOn = false
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