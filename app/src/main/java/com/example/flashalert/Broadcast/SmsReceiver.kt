import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import androidx.core.content.ContextCompat
import com.example.flashalert.Broadcast.FlashLight

class SmsReceiver : BroadcastReceiver() {

    private val flashLight = FlashLight()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            if (smsMessages != null && smsMessages.isNotEmpty()) {
                flashLight.flash(context!!, true, 500, 500, 10)
            }
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
