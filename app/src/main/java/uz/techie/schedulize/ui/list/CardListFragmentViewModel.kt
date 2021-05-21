package uz.techie.schedulize.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.techie.schedulize.models.Subject
import uz.techie.schedulize.repositories.SubjectRepository
import javax.inject.Inject

@HiltViewModel
class CardListFragmentViewModel @Inject constructor(private val repository: SubjectRepository) : ViewModel() {
    val subjectsOfDayLiveData = MutableLiveData<List<Subject>>()

    fun loadData(day:Int){
        viewModelScope.launch {
            repository.getAllSubjectsOfDay(day).collect{list:List<Subject> ->
                subjectsOfDayLiveData.postValue(list)
            }
        }
    }
}