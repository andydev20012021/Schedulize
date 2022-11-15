package uz.techie.schedulize.utils.extentions

import android.app.Activity
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    this.window.statusBarColor = color
}
