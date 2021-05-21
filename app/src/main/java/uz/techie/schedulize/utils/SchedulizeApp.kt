package uz.techie.schedulize.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SchedulizeApp:Application() {
    override fun onCreate() {
        super.onCreate()
//        val pref = PreferenceManager.getDefaultSharedPreferences(this)
//        when(pref.getInt("theme",-1)){
//            1 ->  {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            2-> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }
//            else -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//            }
//        }
    }
    
}