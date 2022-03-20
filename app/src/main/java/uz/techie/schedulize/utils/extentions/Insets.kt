package uz.techie.schedulize.utils.extentions

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding

val WindowInsetsCompat.systemBarsInsets get() = this.getInsets(WindowInsetsCompat.Type.systemBars())
val WindowInsetsCompat.statusBarsInsets get() = this.getInsets(WindowInsetsCompat.Type.statusBars())
val WindowInsetsCompat.navigationBarsInsets get() = this.getInsets(WindowInsetsCompat.Type.navigationBars())
val WindowInsetsCompat.imeInsets get() = this.getInsets(WindowInsetsCompat.Type.ime())
val WindowInsetsCompat.captionBarInsets get() = this.getInsets(WindowInsetsCompat.Type.captionBar())
val WindowInsetsCompat.systemGesturesInsets get() = this.getInsets(WindowInsetsCompat.Type.systemGestures())

fun View.doOnApplyWindowInsets(
    block: View.(windowInsets: WindowInsetsCompat) -> WindowInsetsCompat
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        block(windowInsets)
    }
    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}