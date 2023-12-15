package com.example.flashalert.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.flashalert.R
import com.example.flashalert.databinding.FragmentLanguageDialogBinding


class LanguageDialogFragment : Fragment() {

    private var _binding :FragmentLanguageDialogBinding?=null
    private val binding get() = _binding
    var navController=NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentLanguageDialogBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.toolbar
        val navController =findNavController()
        toolbar?.title = "Language"
        toolbar?.setNavigationOnClickListener {
            navController.navigate(R.id.languageToSetting)
        }



    }
}