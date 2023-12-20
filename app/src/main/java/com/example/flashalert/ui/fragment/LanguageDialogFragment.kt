package com.example.flashalert.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.work.Configuration
import com.example.flashalert.MainActivity
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentLanguageDialogBinding
import com.example.flashalert.prefence.MyPreferences
import java.util.Locale


class LanguageDialogFragment : Fragment() {

    private var _binding: FragmentLanguageDialogBinding? = null
    private val binding get() = _binding
    var navController = NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLanguageDialogBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.toolbar
        val navController = findNavController()
        toolbar?.title = "Language"
        toolbar?.setNavigationOnClickListener {
            navController.navigate(R.id.languageToSetting)
        }

        binding!!.rlUnited.setOnClickListener { changeLanguage("en", "United") }
        binding!!.rlGerman.setOnClickListener { changeLanguage("de", "German") }
        binding!!.rlSpanish.setOnClickListener { changeLanguage("es", "Spanish") }
        binding!!.rlIndia.setOnClickListener { changeLanguage("hi", "India") }
        binding!!.rlItalian.setOnClickListener { changeLanguage("it", "Italy") }
        binding!!.rlJapanese.setOnClickListener { changeLanguage("ja", "Japan") }
        binding!!.rlPolish.setOnClickListener { changeLanguage("pl", "Polish") }
        binding!!.rlPortugal.setOnClickListener { changeLanguage("pt", "Portugal") }
        binding!!.rlVietnam.setOnClickListener { changeLanguage("vi", "Viatnamase") }
    }

    private fun changeLanguage(languageCode: String, CountryName: String) {
        Log.d("ChangeLanguage", "Adım 1")
        val languageManager = MyPreferences.getInstance(requireContext())
        languageManager.prefLanguage = languageCode
        languageManager.prefLanguageName = CountryName

        Log.d("ChangeLanguage", "Adım 2")

        // Yeni dil için locale oluştur
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        Log.d("ChangeLanguage", "Adım 3")
        // Yeni configuration oluştur ve locale'ı ayarla
        val configuration = android.content.res.Configuration()
        configuration.setLocale(locale)
        Log.d("ChangeLanguage", "Adım 4")
        // Uygulamanın resources'unu güncelle
        val context: Context = requireContext()
        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
        Log.d("ChangeLanguage", "Adım 5")
        // Dil tercihlerini kaydet
        languageManager.saveLanguagePreferences()
        languageManager.loadLanguagePreferences()

        // Yeni dil ayarlarını yükleyerek MainActivity'i başlat
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
        Log.d("ChangeLanguage", "Adım 7")
    }



}