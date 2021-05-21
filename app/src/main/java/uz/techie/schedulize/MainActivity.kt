package uz.techie.schedulize

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import uz.techie.schedulize.utils.StatusBarSetColorCallBack

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), StatusBarSetColorCallBack {
    lateinit var toolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

    }

    override fun setStatusBarColor(color: Int) {
        window.setStatusBarColor(ContextCompat.getColor(this,color))

    }
}