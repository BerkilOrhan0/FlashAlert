package com.example.flashalert.Broadcast

import com.example.flashalert.Broadcast.FlashLight
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class CallReceiver(private val context: Context) : BroadcastReceiver() {

    private var flashLight= FlashLight()
    private var isInCall = false

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    // Gelen arama durumu
                    isInCall = true
                    startFlash()
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    // Cevaplanan arama durumu
                    if (isInCall) {
                        stopFlash()
                    }
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    // Arama sona erdi durumu
                    if (isInCall) {
                        isInCall = false
                        stopFlash()
                    }
                }
            }
        }
    }

    private fun startFlash() {
         // com.example.flashalert.Broadcast.FlashLight sınıfındaki initialize metodu eklendi
        flashLight?.isFlashOn = true
        flashLight?.flash(context,
            onDelay = 500,
            offDelay = 500,
            count = -1
        )
    }

    private fun stopFlash() {
        flashLight?.isFlashOn = false

    }
}
