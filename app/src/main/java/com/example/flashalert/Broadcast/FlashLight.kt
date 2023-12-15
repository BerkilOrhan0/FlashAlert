package com.example.flashalert.Broadcast


import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.widget.Toast

class FlashLight {

    var isFlashOn = false
     private lateinit var context:Context

    // Flash'ı açıp kapatan fonksiyon
    fun flash(
        context: Context,
        on: Boolean,
        onDelay: Long,
        offDelay: Long,
        count: Int,
        callback: (() -> Unit)? = null
    ) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        // Flash özelliği ve kullanılabilirliği kontrolü
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            Toast.makeText(context, "Cihazınızda flash özelliği bulunmuyor", Toast.LENGTH_SHORT).show()
            return
        }
        if (!cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.FLASH_INFO_AVAILABLE)!!) {
            Toast.makeText(context, "Flash kullanılamıyor", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            cameraManager.setTorchMode(cameraId, on)
        } catch (e: CameraAccessException) {
            Toast.makeText(context, "Kamera flashına erişilemiyor", Toast.LENGTH_SHORT).show()
            return
        }

        if (count > 0) {
            val handler = android.os.Handler()
            handler.postDelayed(
                {
                    flash(
                        context,
                        !on,
                        onDelay,
                        offDelay,
                        count - 1,
                        callback
                    )
                },
                (if (on) onDelay else offDelay)
            )
        }
    }

    fun flashOff(callback: (() -> Unit)? = null) {
        try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, false)
        } catch (e: Exception) {
            // Flash'ı kapatırken bir hata oluştuğunda yapılacak işlemler
            e.printStackTrace()
        } finally {
            callback?.invoke()
        }
    }
}
