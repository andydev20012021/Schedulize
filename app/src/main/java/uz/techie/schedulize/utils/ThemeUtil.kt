package uz.techie.schedulize.utils

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import uz.techie.schedulize.R

enum class ThemeUtil(val id: Int, val mode:Int,@StringRes val themeName: Int) {
    MODE_NIGHT_FOLLOW_SYSTEM(0,AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,R.string.theme_system),
    MODE_NIGHT_NO(1,AppCompatDelegate.MODE_NIGHT_NO, R.string.theme_light),
    MODE_NIGHT_YES(2,AppCompatDelegate.MODE_NIGHT_YES,R.string.theme_dark);

    companion object{
        const val name = "theme"

        fun findById(id: Int) = values().find{ it.id == id} ?: MODE_NIGHT_FOLLOW_SYSTEM
    }
}