package com.example.flashalert.ui.fragment

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Switch
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flashalert.Broadcast.FlashLight
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentFlashCallBinding
import com.example.flashalert.databinding.FragmentHomeBinding
import com.example.flashalert.prefence.MyPreferences
import com.example.flashalert.viewmodel.CallViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

            // Switch durumunu MyPreferences sınıfından okuma
            val flashOnCallEnabled = preferences.prefSwitchValue

            if (isChecked) {
                updateUI(true)
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
                        val testoffDelay = binding?.seekbarCallOnDelay?.progress ?: 0
                        val testOnDelay = binding?.seekbarCallOffDelay?.progress ?: 0
                        // Flashı aç
                        flashlight.flash(
                            requireContext(), false,
                            testOnDelay.toLong(),
                            testoffDelay.toLong(),
                            10
                        )
                        // On delay süresi kadar bekle
                        delay(testOnDelay.toLong())
                        // Flashı kapat
                        flashlight.flash(
                            requireContext(), false,
                            testOnDelay.toLong(),
                            testoffDelay.toLong(),
                            0
                        )
                        // Off delay süresi kadar bekle
                        delay(testoffDelay.toLong())
                    }
                }
                binding?.btnCallFlashTest?.text = "Stop"
            } else {
                // Coroutine durdur
                flashJob?.cancel()
                flashlight.flash(requireContext(), false, 0, 0, 0)
                binding?.btnCallFlashTest?.text = "start"
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
