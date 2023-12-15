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
import com.example.flashalert.databinding.FragmentNotificationBinding
import com.example.flashalert.viewmodel.NotificationViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding

    val viewModel = NotificationViewModel()
    var flashReceiver = FlashLight()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.toolbar
        val navController = findNavController()
        toolbar?.setupWithNavController(navController)
        toolbar?.setTitle("Notification")
        toolbar?.setNavigationOnClickListener {
            navController.navigate(R.id.notificationToHome)
        }

        viewModel.onDelay.observe(viewLifecycleOwner) { value ->
            binding?.tvNotifacationOnDelay?.text = value.toString()

        }

        viewModel.offDelay.observe(viewLifecycleOwner) { value ->
            binding?.tvNotifacationOffDelay?.text = value.toString()

        }

        viewModel.callSwitch.observe(viewLifecycleOwner) { value ->
            binding?.swNotification?.isChecked = value

        }

        binding?.seekbarNotifacationOffDelay?.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onDelay.value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding?.rlSelect?.setOnClickListener {

        }

        binding?.seekbarNotifacationOffDelay?.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.offDelay.value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding?.btnFlashAppTest?.setOnClickListener {
            val flashJob:Job? = null
            flashReceiver.isFlashOn = !flashReceiver.isFlashOn
            if (flashReceiver.isFlashOn) {
                // Coroutine başlat
                lifecycleScope.launch {
                    // Sonsuz döngü
                    while (flashReceiver.isFlashOn) {
                        // Seekbar değerlerini al
                        val testonDelay = binding?.seekbarNotifacitonOnDelay?.progress ?: 0
                        val testoffDelay = binding?.seekbarNotifacationOffDelay?.progress ?: 0
                        // Flashı aç
                        flashReceiver.flash( requireContext(),false, 0, 0, 10)
                        // On delay süresi kadar bekle
                        delay(testoffDelay.toLong())
                        // Flashı kapat
                        flashReceiver.flash( requireContext(),true, 0, 0, 10)
                        // Off delay süresi kadar bekle
                        delay(testoffDelay.toLong())
                    }
                }
                binding?.btnFlashAppTest?.text = "Stop"
            } else {
                // Coroutine durdur
                flashJob?.cancel()
                binding?.btnFlashAppTest?.text = "Start"
            }
        }


    }

}