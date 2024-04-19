package uz.gita.bootcamp.abdulbosit.apps.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import uz.gita.bootcamp.abdulbosit.apps.data.MyPref

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MyPref.init(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}