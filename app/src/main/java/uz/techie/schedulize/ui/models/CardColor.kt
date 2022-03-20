package uz.techie.schedulize.ui.models

import androidx.annotation.ColorRes
import uz.techie.schedulize.R

enum class CardColor(val id: Int, val colorName: String, @ColorRes val colorRes: Int) {
    DEFAULT(0, "default", R.color.card_color_default),
    RED(1, "red", R.color.card_color_red),
    ORANGE(2, "orange", R.color.card_color_orange),
    YELLOW(3, "yellow", R.color.card_color_yellow),
    GREEN(4, "green", R.color.card_color_green),
    BLUE_GREEN(5, "blue_green", R.color.card_color_blue_green),
    BLUE(6, "blue", R.color.card_color_blue),
    DARK_BLUE(7, "dark_blue", R.color.card_color_dark_blue),
    VIOLET(8, "violet", R.color.card_color_violet),
    PINK(9, "pink", R.color.card_color_pink),
    BROWN(10, "brown", R.color.card_color_brown),
    GREY(11, "grey", R.color.card_color_grey);

    companion object Card {
        fun findByColorName(colorName: String) =
            values().find { it.colorName == colorName } ?: DEFAULT

        fun findById(id: Int) =
            values().find { it.id == id } ?: DEFAULT
    }
}