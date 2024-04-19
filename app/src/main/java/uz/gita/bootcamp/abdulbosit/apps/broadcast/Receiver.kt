package uz.gita.bootcamp.abdulbosit.apps.broadcast

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import uz.gita.bootcamp.abdulbosit.apps.data.MyPref

class Receiver : BroadcastReceiver() {
    private val pref = MyPref
    private var listener: ((String) -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
            BluetoothAdapter.STATE_ON -> {
                if (pref.getTag("bluetooth"))
                    listener?.invoke("BLUETOOTH ON")
            }

            BluetoothAdapter.STATE_OFF -> {
                if (pref.getTag("bluetooth"))
                    listener?.invoke("BLUETOOTH OFF")
            }
        }

        when (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)) {
            WifiManager.WIFI_STATE_ENABLED -> {
                if (pref.getTag("wifi"))
                    listener?.invoke("WIFI ON")
            }

            WifiManager.WIFI_STATE_DISABLED -> {
                if (pref.getTag("wifi"))
                    listener?.invoke("WIFI OFF")
            }
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            if (pref.getTag("mobile_data")) {
                listener?.invoke("MOBILE DATA ON")
            }
        } else {
            if (pref.getTag("mobile_data")) {
                listener?.invoke("MOBILE DATA OFF")
            }
        }

        when (intent.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                if (pref.getTag("battery"))
                    listener?.invoke("POWER CONNECTED")
            }

            Intent.ACTION_POWER_DISCONNECTED -> {
                if (pref.getTag("battery"))
                    listener?.invoke("POWER DISCONNECTED")
            }

            Intent.ACTION_SCREEN_ON -> {
                if (pref.getTag("screen"))
                    listener?.invoke("SCREEN ON")
            }

            Intent.ACTION_SCREEN_OFF -> {
                if (pref.getTag("screen"))
                    listener?.invoke("SCREEN OFF")
            }

            ConnectivityManager.CONNECTIVITY_ACTION -> {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                val networkState = activeNetworkInfo?.isConnectedOrConnecting == true

                if (pref.getTag("internet")) {
                    if (networkState)
                        listener?.invoke("INTERNET CONNECTED")
                    else
                        listener?.invoke("INTERNET DISCONNECTED")
                }

            }

            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                if (pref.getTag("pilot")) {
                    val isAirplaneModeOn = intent.getBooleanExtra("state", false) // Получаем текущее состояние режима полёта
                    if (isAirplaneModeOn)
                        listener?.invoke("AIRPLANE MODE ON")
                    else
                        listener?.invoke("AIRPLANE MODE OFF")
                }
            }

            Intent.ACTION_TIMEZONE_CHANGED -> {
                if (pref.getTag("time_zone")) {
                    listener?.invoke("TIME ZONE CHANGED")
                }
            }

            Intent.ACTION_USER_PRESENT -> {
                if (pref.getTag("unlock")) {
                    listener?.invoke("DEVICE UNLOCKED")
                }
            }

            Intent.ACTION_SHUTDOWN -> {
                if (pref.getTag("shutdown")) {
                    listener?.invoke("DEVICE SHUTTING DOWN")
                }
            }

            Intent.ACTION_LOCALE_CHANGED -> {
                if (pref.getTag("language")) {
                    listener?.invoke("DEVICE LANGUAGE CHANGED")
                }
            }
        }
    }

    fun getActionMessage(block: (String) -> Unit) {
        listener = block
    }
}