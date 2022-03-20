package uz.techie.schedulize.ui.screens.cardview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.techie.schedulize.db.models.DBSubjectModel
import uz.techie.schedulize.repositories.SubjectRepository
import uz.techie.schedulize.ui.models.emptySubjectModel
import uz.techie.schedulize.ui.models.maper.mapToDBSubjectModel
import uz.techie.schedulize.ui.models.maper.mapToUIModel
import javax.inject.Inject

@HiltViewModel
class CardViewScreenViewModel @Inject constructor(private val repository: SubjectRepository) :
    ViewModel() {
    private val _subject = MutableStateFlow(emptySubjectModel)
    val subject get() = _subject.asStateFlow()

    fun deleteSubject() {
        viewModelScope.launch {
            if (subject.value.id != null)
                repository.deleteSubject(subject.value.mapToDBSubjectModel())
        }
    }

    fun loadData(id: Int) {
        viewModelScope.launch {
            repository.getSubjectById(id)
                .map { it.mapToUIModel() }
                .collect { subject ->
                    _subject.value = subject
                }
        }
    }
}