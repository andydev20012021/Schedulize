package uz.techie.schedulize.app

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.akexorcist.localizationactivity.ui.LocalizationApplication
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import uz.techie.schedulize.utils.ThemeUtil
import java.util.*

@HiltAndroidApp
class SchedulizeApp : LocalizationApplication() {


    override fun getDefaultLanguage(base: Context): Locale = Locale.getDefault()

    override fun onCreate() {
        super.onCreate()
        initFirebase()
        initThemeMode()
    }

    private fun initFirebase() {
        FirebaseAnalytics.getInstance(this)

    }

    private fun initThemeMode() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val themeId = pref.getInt(ThemeUtil.name, ThemeUtil.MODE_NIGHT_FOLLOW_SYSTEM.id)
        val theme = ThemeUtil.findById(themeId)
        AppCompatDelegate.setDefaultNightMode(theme.mode)
    }
}