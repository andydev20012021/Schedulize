package uz.techie.schedulize.ui.models

sealed class ModelResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ModelResult<T>()
    data class Error(val error: String) : ModelResult<Nothing>()
    object Loading : ModelResult<Nothing>()
}
