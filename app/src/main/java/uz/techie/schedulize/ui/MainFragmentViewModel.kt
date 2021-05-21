package uz.techie.schedulize.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.techie.schedulize.R
import uz.techie.schedulize.models.Subject
import uz.techie.schedulize.repositories.SubjectRepository
import uz.techie.schedulize.utils.SubjectPeriod
import java.sql.Time
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject constructor(private val repository: SubjectRepository) :
    ViewModel() {
    private val TAG = MainFragmentViewModel::class.java.canonicalName
    val isSubjectDataSourceIsEmptyLiveData = MutableLiveData<Boolean>()

    //getting list of day from repository
    val listOfDayOfWeekFromRepositoryLiveData = MutableLiveData<List<Int>>()

    //getting names of day from resource
    val listNamesOfDayResource = mutableListOf<String>()

    init {
        viewModelScope.launch {
            repository.getDaysOfWeek().collect {list ->
                isSubjectDataSourceIsEmptyLiveData.postValue(list.isEmpty())
                listOfDayOfWeekFromRepositoryLiveData.postValue(list)
                list.forEach {
                    Log.d(TAG,"day = $it")
                }
            }

        }
    }
}