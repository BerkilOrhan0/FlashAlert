package com.example.flashalert.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flashalert.ManagePermissions

class NotificationViewModel:ViewModel() {

    val onDelay = MutableLiveData<Int>()
    val offDelay = MutableLiveData<Int>()
    val callSwitch = MutableLiveData<Boolean>()

    private lateinit var managePermissions: ManagePermissions
    private val PermissionsRequestCode = 123





    fun checkPermissions(isChecked:Boolean,requireActivity: Activity){
        val list = listOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS
        )

        if (isChecked){
            managePermissions =
                ManagePermissions(requireActivity,list,PermissionsRequestCode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                managePermissions.checkPermissions()
            }
        }
    }

}