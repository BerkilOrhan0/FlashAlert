package com.example.flashalert.ui.fragment

import com.example.flashalert.Broadcast.FlashLight
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentFlashCallBinding
import com.example.flashalert.databinding.FragmentHomeBinding
import com.example.flashalert.prefence.MyPreferences
import com.example.flashalert.viewmodel.CallViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FlashCallFragment : Fragment() {

    private var _binding: FragmentFlashCallBinding? = null
    private val binding get() = _binding
    val viewModel = CallViewModel()
    private val flashlight = FlashLight()
    var switchStatus = false
    private lateinit var myPreferences: MyPreferences
    private var homeBinding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFlashCallBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onDelay.observe(viewLifecycleOwner) { value ->
            binding?.tvCallOnDelay?.text = value.toString()
        }
        viewModel.offDelay.observe(viewLifecycleOwner) { value ->
            binding?.tvOffCallDelay?.text = value.toString()
        }
        viewModel.callSwitch.observe(viewLifecycleOwner) { value ->
            binding?.swCall?.isChecked = value

        }
        val toolbar = binding?.toolbar
        val navController = findNavController()
        toolbar?.setupWithNavController(navController)
        toolbar?.setTitle("Flash on Call")
        toolbar?.setNavigationOnClickListener {
            navController.navigate(R.id.callToHome)
        }

        // Call Time On SeekBar'ı için okuma işlemi ve TextView'ı güncelleme
        val callTimeOnSeekBarValue = MyPreferences.getInstance(requireContext()).prefCallTimeOn
        binding?.seekbarCallOnDelay?.progress = callTimeOnSeekBarValue.toInt()
        binding?.tvCallOnDelay?.text = callTimeOnSeekBarValue.toString()

        binding?.seekbarCallOnDelay?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onDelay.value = progress
                // Progress değerini MyPreferences ile kaydetme
                MyPreferences.getInstance(requireContext())
                    .putSeekBarValue(MyPreferences.CALL_TIME_ON, progress.toLong())
                // TextView'ı güncelleme
                binding?.tvCallOnDelay?.text = progress.toString()
                if (flashlight.isFlashOn) {
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.onDelay.value
            }
        })

        // Call Time Off SeekBar'ı için okuma işlemi ve TextView'ı güncelleme
        val callTimeOffSeekBarValue = MyPreferences.getInstance(requireContext()).prefCallTimeOff
        binding?.seekbarCallOffDelay?.progress = callTimeOffSeekBarValue.toInt()
        binding?.tvOffCallDelay?.text = callTimeOffSeekBarValue.toString()

        binding?.seekbarCallOffDelay?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.offDelay.value = progress
                // Progress değerini MyPreferences ile kaydetme
                MyPreferences.getInstance(requireContext())
                    .putSeekBarValue(MyPreferences.CALL_TIME_OFF, progress.toLong())
                // TextView'ı güncelleme
                binding?.tvOffCallDelay?.text = progress.toString()

             
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Burada ek bir işlem yapmanıza gerek yok, çünkü progress değeri zaten yukarıda kaydedildi
            }
        })


        binding?.swCall?.setOnCheckedChangeListener { _, isChecked ->
            // Switch durumunu MyPreferences sınıfında kaydetme
            val preferences = MyPreferences.getInstance(requireContext())
            preferences.prefSwitchValue = isChecked
            updateUI(false)
            // Switch durumunu MyPreferences sınıfından okuma
            val flashOnCallEnabled = preferences.prefSwitchValue

            if (isChecked) {
                binding?.tvStatus?.text = "On"
                viewModel.checkPermissions(true, requireActivity())
                homeBinding?.tvHomeCallStatus?.text = "On"
                homeBinding?.icPhone?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.light_switch_true_color),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                homeBinding?.tvHomeCallStatus?.text = "Off"
                homeBinding?.icPhone?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.light_switch_false_color),
                    PorterDuff.Mode.SRC_IN
                )
                binding?.tvStatus?.text = "Off"
            }

            // Switch durumunu MyPreferences sınıfında kaydetme (yeniden güncelleme)
            MyPreferences.getInstance(requireContext()).prefSwitchValue = isChecked
        }




        binding?.btnCallFlashTest?.setOnClickListener {
            flashlight.isFlashOn = !flashlight.isFlashOn

            if (flashlight.isFlashOn) {
                val testOffDelay = binding?.seekbarCallOffDelay?.progress?.toLong() ?: 500
                val testOnDelay = binding?.seekbarCallOnDelay?.progress?.toLong() ?: 500

                lifecycleScope.launch {
                    while (flashlight.isFlashOn) {
                        flashlight.flash(
                            context,
                            onDelay = testOnDelay,
                            offDelay = testOffDelay,
                            count = 10
                        )
                        delay(testOnDelay)
                        delay(testOffDelay)
                    }
                }
                binding?.btnCallFlashTest?.text = "Stop"
            } else {
                binding?.btnCallFlashTest?.text = "Start"
            }
        }

    }
        fun updateUI(isChecked: Boolean) {
        val iconColor = if (isChecked) {
            // Switch açıkken ikon rengi
            ContextCompat.getColor(requireContext(), R.color.light_switch_true_color)
        } else {
            // Switch kapalıyken ikon rengi
            ContextCompat.getColor(requireContext(), R.color.light_switch_false_color)
        }

        // Ikonu güncelle
        binding?.icPhone?.setColorFilter(iconColor)
    }
}


