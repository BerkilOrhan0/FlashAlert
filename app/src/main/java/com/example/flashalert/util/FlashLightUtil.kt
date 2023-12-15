package com.example.flashalert.util

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.flashalert.prefence.MyPreferences

class FlashLightUtil private constructor(private val mContext: Context) {
    private var cameraId: String? = null
    private val handler: Handler = Handler(Looper.getMainLooper())
    private var isRepeating = false
    private var isTurnOn = false
    private var mCamera: CameraManager? = null
    private var timeOff: Long = 0
    private var timeOn: Long = 0

    init {
        initCamera()
    }

    private val camera: CameraManager?
        get() {
            if (mCamera == null) {
                initCamera()
            }
            return mCamera
        }

    private fun initCamera() {
        try {
            if (mCamera == null) {
                mCamera = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
                val cameraIdList = mCamera?.cameraIdList ?: emptyArray()
                if (cameraIdList.isNotEmpty()) {
                    cameraId = cameraIdList[0]
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun turnOnFlash() {
        try {
            isTurnOn = true
            camera?.setTorchMode(cameraId!!, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun turnOffFlash() {
        try {
            isTurnOn = false
            camera?.setTorchMode(cameraId!!, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val isCameraSupportedForFlashlight: Boolean
        get() {
            val hasSystemFeature = mContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
            if (!hasSystemFeature) {
                Toast.makeText(mContext, "Flashlight is not available on this device", Toast.LENGTH_SHORT).show()
            }
            return hasSystemFeature
        }

    fun repeat(comeFrom: Int) {
        try {
            if (isRepeating) {
                return
            }
            isRepeating = true

            when (comeFrom) {
                0 -> {
                    timeOn = MyPreferences.getInstance(mContext).prefCallTimeOn
                    timeOff = MyPreferences.getInstance(mContext).prefCallTimeOff
                }
                1 -> {
                    timeOn = MyPreferences.getInstance(mContext).prefSmsTimeOn
                    timeOff = MyPreferences.getInstance(mContext).prefSmsTimeOff
                }
                2 -> {
                    timeOn = MyPreferences.getInstance(mContext).prefNotificationTimeOn
                    timeOff = MyPreferences.getInstance(mContext).prefNotificationTimeOff
                }
            }

            handler.removeCallbacksAndMessages(null)
            isTurnOn = false

            handler.postDelayed(
                {
                    repeatRunnable()
                },
                timeOn
            )
        } catch (e: Exception) {
            e.printStackTrace()
            handler.removeCallbacksAndMessages(null)
        }
    }

    private fun repeatRunnable() {
        toggleFlash()
        handler.postDelayed(
            {
                repeatRunnable()
            },
            if (isTurnOn) timeOn else timeOff
        )
    }

    private fun toggleFlash() {
        try {
            isTurnOn = !isTurnOn
            camera?.setTorchMode(cameraId!!, isTurnOn)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeCallBack() {
        isRepeating = false
        handler.removeCallbacksAndMessages(null)
    }

    fun turnOffFlashAndRemoveCallBack() {
        try {
            removeCallBack()
            isTurnOn = false
            camera?.setTorchMode(cameraId!!, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private var instance: FlashLightUtil? = null

        fun getInstance(context: Context): FlashLightUtil {
            if (instance == null) {
                instance = FlashLightUtil(context)
            }
            return instance!!
        }
    }
}
