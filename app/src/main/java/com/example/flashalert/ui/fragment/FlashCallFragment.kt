package com.example.flashalert.ui.fragment

import android.os.Bundle
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
    private lateinit  var myPreferences:MyPreferences


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

        binding?.seekbarCallOnDelay?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.onDelay.value = progress

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.onDelay.value
            }

        })

        binding?.seekbarCallOffDelay?.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.offDelay.value = progress

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

                val seekBarValue = seekBar?.progress ?: 0
                MyPreferences.getInstance(requireContext()).putSeekBarValue(MyPreferences.CALL_TIME_OFF,
                    seekBarValue.toLong()
                )

            }

        })

        binding?.swCall?.setOnCheckedChangeListener { buttonView, isChecked ->

            switchStatus = isChecked
            if (isChecked) {
                updateUI()
                binding?.tvStatus?.text = "On"
                viewModel.checkPermissions(true, requireActivity())
            } else {

                binding?.tvStatus?.text ="Off"
            }
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
                flashlight.flash(requireContext(),false, 0, 0, 10)
                binding?.btnCallFlashTest?.text = "start"
            }
        }

    }

    fun updateUI() {
        val iconColor = if (switchStatus) {
            // Switch açıkken ikon rengi
            ContextCompat.getColor(requireContext(), R.color.light_switch_true_color)
        } else {
            // Switch kapalıyken ikon rengi
            ContextCompat.getColor(requireContext(), R.color.light_switch_false_color)
        }

        // Ikonu güncelle (ImageView olarak adlandırıldığını varsayıyoruz)
        binding?.icPhone?.setColorFilter(iconColor)
    }

}
