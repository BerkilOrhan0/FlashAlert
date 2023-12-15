package com.example.flashalert.Broadcast


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager


class CallReceiver() : BroadcastReceiver() {

    private val flashLight = FlashLight()

    private var isInCall = false

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // Gelen arama durumu
                isInCall = true
                flashLight.flash(context!!, true, 500, 500, -1) // Sonsuz döngü (-1)
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                // Cevaplanan arama durumu
                if (isInCall) {
                    flashLight.flashOff()
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                // Arama sona erdi durumu
                if (isInCall) {
                    isInCall = false
                    flashLight.flashOff()
                }
            }
        }
    }
}





