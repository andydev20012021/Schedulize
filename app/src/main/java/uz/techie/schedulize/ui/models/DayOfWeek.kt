package uz.techie.schedulize.ui.models

import androidx.annotation.StringRes
import uz.techie.schedulize.R
import java.io.Serializable

enum class DayOfWeek(val id: Int, val dayName: String, @StringRes val dayRes: Int):Serializable {
    MONDAY(1, "monday", R.string.monday),
    TUESDAY(2, "tuesday", R.string.tuesday),
    WEDNESDAY(3, "wednesday", R.string.wednesday),
    THURSDAY(4, "thursday", R.string.thursday),
    FRIDAY(5, "friday", R.string.friday),
    SATURDAY(6, "saturday", R.string.saturday),
    SUNDAY(0, "sunday", R.string.sunday);

    companion object Day {
        fun findByName(dayName: String) = values().find { it.dayName == dayName } ?: SUNDAY
        fun findById(id: Int) = values().find { it.id == id } ?: SUNDAY
    }
}