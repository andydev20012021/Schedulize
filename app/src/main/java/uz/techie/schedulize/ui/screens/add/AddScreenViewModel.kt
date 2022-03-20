package uz.techie.schedulize.ui.screens.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.techie.schedulize.R
import uz.techie.schedulize.repositories.SubjectRepository
import uz.techie.schedulize.ui.models.SubjectModel
import uz.techie.schedulize.ui.models.emptySubjectModel
import uz.techie.schedulize.ui.models.maper.mapToDBSubjectModel
import uz.techie.schedulize.ui.models.maper.mapToUIModel
import javax.inject.Inject
import javax.security.auth.Subject

@HiltViewModel
class AddScreenViewModel @Inject constructor(private val repository: SubjectRepository) :
    ViewModel() {
    var isEditing = false
    var subjectId:Int? = null
    private val _subject = MutableStateFlow(emptySubjectModel)
    val subject get() = _subject.asStateFlow()

    fun getSubjectByIdIfEditing(id: Int) {
        subjectId = id
        viewModelScope.launch {
            repository.getSubjectById(id)
                .map { it.mapToUIModel() }
                .collect {
                    _subject.value = it
                }
        }
    }

    fun saveSubject(subject: SubjectModel) {
        viewModelScope.launch {
            if (isEditing) repository.updateSubject(subject.mapToDBSubjectModel())
            else repository.insertSubject(subject.mapToDBSubjectModel())
        }
    }
}