
import android.provider.Telephony
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.example.flashalert.Broadcast.FlashLight

class SmsReceiver(private val context: Context) : BroadcastReceiver() {

    private var flashLight= FlashLight()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            if (smsMessages != null && smsMessages.isNotEmpty()) {
                handleSmsReceived(context)
            }
        }
    }

    private fun handleSmsReceived(context: Context?) {
        context?.let {
            flashLight?.isFlashOn = true
            flashLight?.flash(context,
                onDelay = 500,
                offDelay = 500,
                count = 10
            )

        }
    }



    companion object {
        fun register(context: Context, smsReceiver: SmsReceiver) {
            val filter = IntentFilter()
            filter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
            context.registerReceiver(smsReceiver, filter)
        }

        fun unregister(context: Context, smsReceiver: SmsReceiver) {
            context.unregisterReceiver(smsReceiver)
        }
    }
}
