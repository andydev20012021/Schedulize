package uz.techie.schedulize.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.techie.schedulize.repositories.SubjectRepository
import uz.techie.schedulize.ui.models.DayOfWeek
import uz.techie.schedulize.ui.models.ModelResult
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: SubjectRepository
) : ViewModel() {
    private val TAG = HomeScreenViewModel::class.java.canonicalName

    private val _listOfDayOfWeek =
        MutableStateFlow<List<DayOfWeek>>(emptyList())
    val listOfDayOfWeek = _listOfDayOfWeek.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getDaysOfWeek()
                .map { listOfDays ->
                    listOfDays.map { DayOfWeek.findByName(it) }
                }
                .collect { list ->
                    _listOfDayOfWeek.value = list
                }
        }
    }
}