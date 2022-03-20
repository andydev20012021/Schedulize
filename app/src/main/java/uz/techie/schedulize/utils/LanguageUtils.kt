package uz.techie.schedulize.utils

import android.content.Context
import androidx.annotation.StringRes
import uz.techie.schedulize.R

enum class LanguageUtils(val id: Int, val code: String, @StringRes val langName: Int) {
    ENGLISH(0, "en", R.string.english),
    RUSSIA(1, "ru", R.string.russia),
    UZBEK(2, "uz", R.string.uzbek);

    companion object {
        fun findById(id: Int) = values().find { it.id == id } ?: ENGLISH
        fun findByCode(code: String) = values().find { it.code == code } ?: ENGLISH
        fun getLanguages(context: Context) = values().map { context.resources.getString(it.langName) }.toTypedArray()
    }
}