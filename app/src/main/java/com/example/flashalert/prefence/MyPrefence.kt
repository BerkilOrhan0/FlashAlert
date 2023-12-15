package com.example.flashalert.prefence

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


class MyPreferences private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARE_PREFERENCES, Context.MODE_PRIVATE)


    fun putSeekBarValue(key: String, value: Long) {
        sharedPreferences.edit {
            putLong(key, value)
        }
    }

    var prefTheme: Int
        get() = sharedPreferences.getInt(PREF_THEME, 0)
        set(value) = sharedPreferences.edit { putInt(PREF_THEME, value) }

    var prefCallTimeOn: Long
        get() = sharedPreferences.getLong(CALL_TIME_ON, 500L)
        set(value) = sharedPreferences.edit { putLong(CALL_TIME_ON, value) }

    var prefCallTimeOff: Long
        get() = sharedPreferences.getLong(CALL_TIME_OFF, 500L)
        set(value) = sharedPreferences.edit { putLong(CALL_TIME_OFF, value) }

    var prefSmsTimeOn: Long
        get() = sharedPreferences.getLong(SMS_TIME_ON, 500L)
        set(value) = sharedPreferences.edit { putLong(SMS_TIME_ON, value) }

    var prefSmsTimeOff: Long
        get() = sharedPreferences.getLong(SMS_TIME_OFF, 500L)
        set(value) = sharedPreferences.edit { putLong(SMS_TIME_OFF, value) }

    var prefNotificationTimeOn: Long
        get() = sharedPreferences.getLong(NOTIFICATION_TIME_ON, 500L)
        set(value) = sharedPreferences.edit { putLong(NOTIFICATION_TIME_ON, value) }

    var prefNotificationTimeOff: Long
        get() = sharedPreferences.getLong(NOTIFICATION_TIME_OFF, 500L)
        set(value) = sharedPreferences.edit { putLong(NOTIFICATION_TIME_OFF, value) }

    var prefFlashOnCall: Boolean
        get() = sharedPreferences.getBoolean(PREF_FLASH_ON_CALL, false)
        set(value) = sharedPreferences.edit { putBoolean(PREF_FLASH_ON_CALL, value) }

    var prefFlashOnSms: Boolean
        get() = sharedPreferences.getBoolean(PREF_FLASH_ON_SMS, false)
        set(value) = sharedPreferences.edit { putBoolean(PREF_FLASH_ON_SMS, value) }

    var prefFlashOnNotification: Boolean
        get() = sharedPreferences.getBoolean(PREF_FLASH_ON_NOTIFICATION, false)
        set(value) = sharedPreferences.edit { putBoolean(PREF_FLASH_ON_NOTIFICATION, value) }

    var prefFlashInSoundMode: Boolean
        get() = sharedPreferences.getBoolean(PREF_FLASH_IN_SOUND_MODE, true)
        set(value) = sharedPreferences.edit { putBoolean(PREF_FLASH_IN_SOUND_MODE, value) }

    var prefFlashInVibrateMode: Boolean
        get() = sharedPreferences.getBoolean(PREF_FLASH_IN_VIBRATE_MODE, true)
        set(value) = sharedPreferences.edit { putBoolean(PREF_FLASH_IN_VIBRATE_MODE, value) }

    var prefFlashInSilentMode: Boolean
        get() = sharedPreferences.getBoolean(PREF_FLASH_IN_SILENT_MODE, true)
        set(value) = sharedPreferences.edit { putBoolean(PREF_FLASH_IN_SILENT_MODE, value) }

    var prefNotFlashWhenScreenOn: Boolean
        get() = sharedPreferences.getBoolean(PREF_NOT_FLASH_WHEN_SCREEN_ON, false)
        set(value) = sharedPreferences.edit { putBoolean(PREF_NOT_FLASH_WHEN_SCREEN_ON, value) }

    var prefRatedApp: Boolean
        get() = sharedPreferences.getBoolean(EXTRA_RATE_APP, false)
        set(value) = sharedPreferences.edit { putBoolean(EXTRA_RATE_APP, value) }

    var prefLanguage: String?
        get() = sharedPreferences.getString(PREF_LANGUAGE, null)
        set(value) = sharedPreferences.edit { putString(PREF_LANGUAGE, value) }

    var prefLanguageName: String?
        get() = sharedPreferences.getString(PREF_LANGUAGE_NAME, null)
        set(value) = sharedPreferences.edit { putString(PREF_LANGUAGE_NAME, value) }

    var prefIsFirstTimeLaunch: Boolean
        get() = sharedPreferences.getBoolean(IS_FIRST_TIME_LAUNCH, true)
        set(value) = sharedPreferences.edit { putBoolean(IS_FIRST_TIME_LAUNCH, value) }

    var prefSelectAppTutorial: Boolean
        get() = sharedPreferences.getBoolean(PREF_SHOW_SELECT_APP_TUTORIAL, true)
        set(value) = sharedPreferences.edit { putBoolean(PREF_SHOW_SELECT_APP_TUTORIAL, value) }

    var prefCntOpenApp: Int
        get() = sharedPreferences.getInt(PREF_CNT_OPEN_APP, 0)
        set(value) = sharedPreferences.edit { putInt(PREF_CNT_OPEN_APP, value) }

    companion object {
        const val CALL_TIME_OFF = "CALL_TIME_OFF"
        private const val CALL_TIME_ON = "CALL_TIME_ON"
        private const val EXTRA_RATE_APP = "EXTRA_RATE_APP"
        private const val IS_FIRST_TIME_LAUNCH = "IS_FIRST_TIME_LAUNCH"
        private const val NOTIFICATION_TIME_OFF = "NOTIFICATION_TIME_OFF"
        private const val NOTIFICATION_TIME_ON = "NOTIFICATION_TIME_ON"
        private const val PREF_CNT_OPEN_APP = "PREF_CNT_OPEN_APP"
        private const val PREF_FLASH_IN_SILENT_MODE = "PREF_FLASH_IN_SILENT_MODE"
        private const val PREF_FLASH_IN_SOUND_MODE = "PREF_FLASH_IN_SOUND_MODE"
        private const val PREF_FLASH_IN_VIBRATE_MODE = "PREF_FLASH_IN_VIBRATE_MODE"
        private const val PREF_FLASH_ON_CALL = "PREF_FLASH_ON_CALL"
        private const val PREF_FLASH_ON_NOTIFICATION = "PREF_FLASH_ON_NOTIFICATION"
        private const val PREF_FLASH_ON_SMS = "PREF_FLASH_ON_SMS"
        private const val PREF_LANGUAGE = "PREF_LANGUAGE"
        private const val PREF_LANGUAGE_NAME = "PREF_LANGUAGE_NAME"
        private const val PREF_NOT_FLASH_WHEN_SCREEN_ON = "PREF_NOT_FLASH_WHEN_SCREEN_ON"
        private const val PREF_SHOW_SELECT_APP_TUTORIAL = "PREF_SHOW_SELECT_APP_TUTORIAL"
        private const val PREF_THEME = "PREF_THEME"
        private const val SHARE_PREFERENCES = "SHARE_PREFERENCES"
        private const val SMS_TIME_OFF = "SMS_TIME_OFF"
        private const val SMS_TIME_ON = "SMS_TIME_ON"

        // Singleton sınıfın tek örneğini tutan değişken
        private var instance: MyPreferences? = null

        // Singleton sınıfın tek örneğini döndüren fonksiyon
        fun getInstance(context: Context): MyPreferences {
            // Eğer instance null ise, yeni bir MyPreferences örneği oluştur ve instance'a atayarak döndür
            return instance ?: synchronized(this) {
                instance ?: MyPreferences(context).also { instance = it }
            }
        }
    }
}
