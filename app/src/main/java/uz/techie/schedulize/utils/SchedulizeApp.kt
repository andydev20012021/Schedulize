package uz.techie.schedulize.utils

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class SchedulizeApp : LocalizationApplication() {
    override fun getDefaultLanguage(base: Context): Locale = Locale.getDefault()

    override fun onCreate() {
        super.onCreate()
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = pref.getInt("theme", 0)
        val themeMode = when (theme) {
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            2 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(theme)
    }
}