package uz.techie.schedulize.utils

import androidx.annotation.ColorRes

interface StatusBarSetColorCallBack {
    abstract fun setStatusBarColor(@ColorRes color:Int)
}