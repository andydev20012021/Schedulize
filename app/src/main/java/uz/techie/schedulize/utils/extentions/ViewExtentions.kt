package uz.techie.schedulize.utils.extentions

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.core.view.*
import kotlin.math.roundToInt

val Int.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

fun View.showGone(show: Boolean): View {
    visibility = if (show) View.VISIBLE else View.GONE
    return this
}

fun View.showInvisible(show: Boolean): View {
    visibility = if (show) View.VISIBLE else View.INVISIBLE
    return this
}

fun View.gone(): View {
    visibility = View.GONE
    return this
}

fun View.invisible(): View {
    visibility = View.INVISIBLE
    return this
}

fun View.visible(): View {
    visibility = View.VISIBLE
    return this
}

fun View.updateMargin(
    @Px left: Int = marginLeft,
    @Px top: Int = marginTop,
    @Px right: Int = marginRight,
    @Px bottom: Int = marginBottom,
) {
    this.updateLayoutParams<ViewGroup.MarginLayoutParams> {
        topMargin = top
        bottomMargin = bottom
        leftMargin = left
        rightMargin = right
    }
}

fun View.updateMargin(@Px size: Int) = updateMargin(size,size,size,size)