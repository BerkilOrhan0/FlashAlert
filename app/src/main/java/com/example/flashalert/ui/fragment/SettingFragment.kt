package com.example.flashalert.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {

    private var _binding : FragmentSettingBinding?=null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(layoutInflater,container,false)
       return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.toolbar
        val navController = findNavController()
        toolbar?.setupWithNavController(navController)
        toolbar?.setTitle("Setting")
        toolbar?.setNavigationOnClickListener {
            navController.navigate(R.id.settingtoHome)
        }

        binding?.rlLanguage?.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.settingToLanguage)
        }




    }


}