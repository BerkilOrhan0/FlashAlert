package com.example.flashalert

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class ManagePermissions(val activity: Activity, val list: List<String>, val code: Int) {

    // İzinleri kontrol eden fonksiyon
    fun checkPermissions() {
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            // Eğer izinler verilmediyse, izinleri isteyelim
            requestPermissions()
        }
    }

    // İzinlerin verilip verilmediğini kontrol eden fonksiyon
    private fun isPermissionsGranted(): Int {
        return list.fold(0) { acc, permission ->
            acc + ContextCompat.checkSelfPermission(activity, permission)
        }
    }

    // İzinleri isteyen fonksiyon
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
    }



    // İzinlerin sonucunu işleyen fonksiyon
    fun processPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        return grantResults.all { it == PackageManager.PERMISSION_GRANTED }
    }
}
