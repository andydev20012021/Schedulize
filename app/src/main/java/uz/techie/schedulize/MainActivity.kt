package uz.techie.schedulize

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import dagger.hilt.android.AndroidEntryPoint
import uz.techie.schedulize.utils.StatusBarSetColorCallBack

@AndroidEntryPoint
class MainActivity : LocalizationActivity(), StatusBarSetColorCallBack {
    lateinit var toolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.navigationBarColor = ContextCompat.getColor(this,R.color.primary)

    }

    override fun setStatusBarColor(@ColorRes color : Int) {
        window.statusBarColor = ContextCompat.getColor(this,color)
    }
}
