package uz.techie.schedulize.ui.screens.main.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.techie.schedulize.repositories.SubjectRepository
import uz.techie.schedulize.ui.models.DayOfWeek
import uz.techie.schedulize.ui.models.SubjectModel
import uz.techie.schedulize.ui.models.maper.mapToUIModel
import javax.inject.Inject

@HiltViewModel
class CardListScreenViewModel @Inject constructor(private val repository: SubjectRepository) :
    ViewModel() {

    private val _listOfSubjects = MutableStateFlow<List<SubjectModel>>(emptyList())
    val listOfSubjects get() = _listOfSubjects.asStateFlow()

    fun loadData(day: DayOfWeek) {
        viewModelScope.launch {
            repository.getAllSubjectsOfDay(day.dayName)
                .map { list ->
                    list.map { it.mapToUIModel() }
                }
                .collect { list ->
                    _listOfSubjects.value = list
                }
        }
    }
}