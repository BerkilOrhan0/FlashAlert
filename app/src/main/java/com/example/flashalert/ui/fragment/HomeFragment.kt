package com.example.flashalert.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentHomeBinding


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

                else -> false
            }
        }


    }

    override fun onResume() {
        super.onResume()
        HomeFragment()
    }

}