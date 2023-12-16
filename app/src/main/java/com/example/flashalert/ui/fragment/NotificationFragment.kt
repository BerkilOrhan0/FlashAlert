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
import com.example.flashalert.databinding.FragmentNotificationBinding
import com.example.flashalert.prefence.MyPreferences
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
            binding?.tvAppOnDelay?.text = value.toString()

        }

        viewModel.offDelay.observe(viewLifecycleOwner) { value ->
            binding?.tvAppOffDelay?.text = value.toString()

        }

        viewModel.callSwitch.observe(viewLifecycleOwner) { value ->
            binding?.swNotification?.isChecked = value

        }


        val notificationTimeOnSeekBarValue =
            MyPreferences.getInstance(requireContext()).prefNotificationTimeOn
        binding?.seekbarNotifacitonOnDelay?.progress = notificationTimeOnSeekBarValue.toInt()
        binding?.tvAppOnDelay?.text = notificationTimeOnSeekBarValue.toString()
        binding?.seekbarNotifacitonOnDelay?.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onDelay.value = progress
                MyPreferences.getInstance(requireContext())
                    .putSeekBarValue(MyPreferences.NOTIFICATION_TIME_ON, progress.toLong())
                // TextView'ı güncelleme
                binding?.tvAppOnDelay?.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding?.rlSelect?.setOnClickListener {

        }

        val notificatiOffTimeOffSeekBarValue =
            MyPreferences.getInstance(requireContext()).prefNotificationTimeOff
        binding?.seekbarNotifacationOffDelay?.progress =notificatiOffTimeOffSeekBarValue.toInt()
        binding?.tvAppOffDelay?.text = notificatiOffTimeOffSeekBarValue.toString()
        binding?.seekbarNotifacationOffDelay?.setOnSeekBarChangeListener(object :
            OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.offDelay.value = progress
                MyPreferences.getInstance(requireContext())
                    .putSeekBarValue(MyPreferences.NOTIFICATION_TIME_OFF, progress.toLong())
                // TextView'ı güncelleme
                binding?.tvAppOffDelay?.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        binding?.btnFlashAppTest?.setOnClickListener {
            val flashJob: Job? = null
            flashReceiver.isFlashOn = !flashReceiver.isFlashOn

            if (flashReceiver.isFlashOn) {
                // Coroutine başlat
                val testOnDelay = binding?.seekbarNotifacitonOnDelay?.progress?.toLong() ?: 0
                val testOffDelay = binding?.seekbarNotifacationOffDelay?.progress?.toLong() ?: 0

                lifecycleScope.launch {
                    // Sonsuz döngü
                    while (flashReceiver.isFlashOn) {
                        // Flash'ı aç
                        flashReceiver.flash(context,
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
                binding?.btnFlashAppTest?.text = "Stop"
            } else {
                // Coroutine durdur
                flashJob?.cancel()
                binding?.btnFlashAppTest?.text = "Start"
            }
        }

    }

}