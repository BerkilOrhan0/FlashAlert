package com.example.flashalert

import SmsReceiver
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.TelephonyManager
import com.example.flashalert.Broadcast.CallReceiver

class MainActivity : AppCompatActivity() {

    private lateinit var callReceiver: CallReceiver
    private lateinit var smsReceiver: SmsReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        // CallReceiver'ı parametrelerle oluşturun
        callReceiver = CallReceiver()

        // IntentFilter'ı oluşturun
        val callFilter = IntentFilter(TelephonyManager.ACTION_PHONE_STATE_CHANGED)

        // CallReceiver'ı kaydedin
        registerReceiver(callReceiver, callFilter)

        // SmsReceiver'ı parametrelerle oluşturun
        smsReceiver = SmsReceiver()

        // IntentFilter'ı oluşturun
        val smsFilter = IntentFilter()
        smsFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)

        // SmsReceiver'ı kaydedin
        SmsReceiver.register(this, smsReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()

        // BroadcastReceiver'ları kayıttan kaldırın
        unregisterReceiver(callReceiver)
        SmsReceiver.unregister(this, smsReceiver)
    }
}
