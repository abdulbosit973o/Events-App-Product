package uz.gita.bootcamp.abdulbosit.apps.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.core.app.NotificationCompat
import uz.gita.bootcamp.abdulbosit.apps.MainActivity
import uz.gita.bootcamp.abdulbosit.apps.util.log
import uz.gita.bootcamp.abdulbosit.apps.R
import uz.gita.bootcamp.abdulbosit.apps.broadcast.Receiver
import java.util.Locale

class EventService : Service(), TextToSpeech.OnInitListener {
    private val channel = "Event"
    private val receiver = Receiver()
    private lateinit var tts: TextToSpeech

    override fun onCreate() {
        super.onCreate()

        val intentFilter = IntentFilter()

        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED)
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED)
        intentFilter.addAction(Intent.ACTION_SCREEN_ON)
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF)
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED)
        intentFilter.addAction(Intent.ACTION_USER_PRESENT)
        intentFilter.addAction(Intent.ACTION_SHUTDOWN)
        intentFilter.addAction(Intent.ACTION_LOCALE_CHANGED)

        registerReceiver(receiver, intentFilter)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            registerReceiver(receiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)

        tts = TextToSpeech(this, this)
        tts.setSpeechRate(1f)


        receiver.getActionMessage { mes ->
            speakOut(mes)
            mes.log()
        }

        createChannel()
        startMyService()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                Log.e("TTS", "Язык не поддерживается")
        } else Log.e("TTS", "Инициализация не удалась")
    }


    private fun speakOut(text: String) = tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

    @SuppressLint("ForegroundServiceType", "LaunchActivityFromNotification")
    private fun startMyService() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification: Notification =
            NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Events")
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .build()

        startForeground(1, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel("Event", channel, NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)

            val service = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            service.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        unregisterReceiver(receiver)
    }

}