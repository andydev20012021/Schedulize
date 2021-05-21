package uz.techie.schedulize.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.techie.schedulize.R
import uz.techie.schedulize.models.Subject
import uz.techie.schedulize.repositories.SubjectRepository
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(private val repository: SubjectRepository) : ViewModel() {
    var isEditingLiveData = MutableLiveData<Boolean>(false)
    val subjectLiveData = MutableLiveData<Subject>()
    var selectedColorResPosition:Int = 0
    lateinit var dayOfWeek:Array<String>
    val arrayOfColorRes =
        listOf<Int>(
            R.color.card_color_default,
            R.color.card_color_red,
            R.color.card_color_orange,
            R.color.card_color_yellow,
            R.color.card_color_green,
            R.color.card_color_blue_green,
            R.color.card_color_blue,
            R.color.card_color_dark_blue,
            R.color.card_color_violet,
            R.color.card_color_pink,
            R.color.card_color_brown,
            R.color.card_color_grey
        )

    fun getSubjectByIdIfEditing(id:Int){
        viewModelScope.launch {
            repository.getSubjectById(id).collect{it:Subject ->
                subjectLiveData.postValue(it)
            }
        }
    }

    fun saveSubject(subject: Subject){
            viewModelScope.launch {
                val isEditing = isEditingLiveData.value

                if (isEditing == true) repository.updateSubject(subject)
                else repository.insertSubject(subject)
            }
    }

}