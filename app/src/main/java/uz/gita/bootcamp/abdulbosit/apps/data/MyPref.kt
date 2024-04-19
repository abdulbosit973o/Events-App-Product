package uz.gita.bootcamp.abdulbosit.apps.data

import android.content.Context
import android.content.SharedPreferences

object MyPref {
    private var pref: SharedPreferences? = null
    fun init(context: Context) {
        if (pref == null)
            pref =context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    fun getTag(id: String) : Boolean =
        pref!!.getBoolean(id, false)

    fun setTag(id: String, check: Boolean) =
        pref!!.edit().putBoolean(id, check).apply()

}