package com.example.flashalert.ui.fragment

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentHomeBinding
import com.example.flashalert.prefence.MyPreferences


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding

    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.toolbar

        binding?.rlFlashPhone?.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.homeToCall)
        }

        binding?.rlFlashSms?.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.homeToSms)
        }

        binding?.rlFlashNotification?.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.homeToNotificitaion)
        }

        navController = findNavController()
        toolbar?.navigationIcon = null
        toolbar?.setupWithNavController(navController)
        toolbar?.title = "Flash Alert"

        toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.settingFragment -> {
                    navController.navigate(R.id.HomeToSetting)
                    true
                }

                R.id.helperFragment -> {
                    navController.navigate(R.id.homeToHelper)
                    true
                }

                R.id.ic_dark_mode -> {
                    toggleDarkMode()
                    updateDarkModeIcon(item)
                    true
                }

                else -> false
            }
        }


    }

    private fun toggleDarkMode() {
        val isNightMode =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        val newMode =
            if (isNightMode) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(newMode)

        saveThemeMode(newMode)

        recreate(requireActivity()) // Aktiviteyi yeniden oluşturarak temayı güncelle
    }

    private fun updateDarkModeIcon(item: MenuItem) {
        val isNightMode =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        if (isNightMode) {
            item.setIcon(R.drawable.ic_moon)
        } else {
            item.setIcon(R.drawable.ic_sun)
        }

        requireActivity().invalidateOptionsMenu()
    }

    private fun saveThemeMode(themeMode: Int) {
        val preferences = MyPreferences.getInstance(requireContext())
        preferences.prefTheme = themeMode

    }
}