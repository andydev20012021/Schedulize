package uz.techie.schedulize

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : LocalizationActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN){
            param("lang",getCurrentLanguage().language)
            param("theme",AppCompatDelegate.getDefaultNightMode().toLong())
        }
    }

    override fun onBackPressed() {
        if (isBackPressable)
            super.onBackPressed()
    }

    companion object {
        var isBackPressable = true
    }
}
