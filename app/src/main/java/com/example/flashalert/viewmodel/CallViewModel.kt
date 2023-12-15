package com.example.flashalert.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashalert.Broadcast.FlashLight
import com.example.flashalert.ManagePermissions
import com.example.flashalert.databinding.FragmentFlashCallBinding

class CallViewModel : ViewModel() {

    private var _binding : FragmentFlashCallBinding?=null
    private val binding get() = _binding

    val onDelay = MutableLiveData<Int>()
    val offDelay = MutableLiveData<Int>()
    val callSwitch = MutableLiveData<Boolean>()

    private val flashReceiver = FlashLight()
    private lateinit var managePermissions: ManagePermissions
    private val PermissionsRequestCode = 123



     fun flash(context: Context, onDelay: Long, offDelay: Long, count: Int) {
        flashReceiver.flash(context, false,onDelay,offDelay,10)
    }




    fun checkPermissions(isChecked: Boolean, requireActivity: Activity) {
        val list = listOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        )

        if (isChecked) {
            managePermissions =
                ManagePermissions(requireActivity, list, PermissionsRequestCode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                managePermissions.checkPermissions()
            }
        }
    }
}
