package uz.techie.schedulize.ui.cardview

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
class CardViewViewModel @Inject constructor(private val repository: SubjectRepository): ViewModel() {
    val subjectLiveData = MutableLiveData<Subject>()

    fun deleteSubject(){
        viewModelScope.launch {
            if (subjectLiveData.value != null)
                repository.deleteSubject(subjectLiveData.value!!)
        }
    }

    fun loadData(id:Int){
        viewModelScope.launch {
              repository.getSubjectById(id).collect{subject:Subject->
                  subjectLiveData.postValue(subject)
              }
        }
    }
}