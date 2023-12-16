package com.example.flashalert.Broadcast

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashLight {

    var isFlashOn = false
    private var flashJob: Job? = null
    private lateinit var mContext: Context

    // Flash'ı açıp kapatan fonksiyon
    fun flash(
        context: Context?,
        onDelay: Long,
        offDelay: Long,
        count: Int,
        callback: (() -> Unit)? = null
    ) {
        mContext = context ?: return // Context null ise işlem yapma

        val cameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]

        // Flash özelliği ve kullanılabilirliği kontrolü
        if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            return
        }
        if (!cameraManager.getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE)!!
        ) {
            return
        }

        flashJob = CoroutineScope(Dispatchers.IO).launch {
            repeat(count) {
                try {
                    // Flash'ı aç
                    withContext(Dispatchers.Main) {
                        cameraManager.setTorchMode(cameraId, true)
                    }
                    delay(onDelay)
                    // Flash'ı kapat
                    withContext(Dispatchers.Main) {
                        cameraManager.setTorchMode(cameraId, false)
                    }
                    delay(offDelay)
                } catch (e: Exception) {
                    // Flash işlemi sırasında hata oluştuğunda yapılacak işlemler
                    e.printStackTrace()
                }
            }
            withContext(Dispatchers.Main) {
                callback?.invoke()
            }
        }

    }
}
