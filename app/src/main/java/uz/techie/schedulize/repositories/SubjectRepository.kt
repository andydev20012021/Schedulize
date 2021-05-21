package uz.techie.schedulize.repositories

import kotlinx.coroutines.flow.Flow
import uz.techie.schedulize.models.Subject
import java.time.DayOfWeek

class SubjectRepository(
    private val subjectLocalDataSource: SubjectLocalDataSource
) {
    suspend fun insertSubject(subject: Subject){
        subjectLocalDataSource.insertSubject(subject)
    }

    suspend fun deleteSubject(subject: Subject){
        subjectLocalDataSource.deleteSubject(subject)
    }

    suspend fun updateSubject(subject: Subject){
        subjectLocalDataSource.updateSubject(subject)
    }

    fun getAllSubjectsOfDay(dayOfWeek: Int):Flow<List<Subject>> = subjectLocalDataSource.getAllSubjectsOfDay(dayOfWeek)

     fun getAllSubjects(): Flow<List<Subject>> = subjectLocalDataSource.getAllSubjects()

    fun getSubjectById(id: Int): Flow<Subject> = subjectLocalDataSource.getSubject(id)

    fun getDaysOfWeek(): Flow<List<Int>> = subjectLocalDataSource.getDaysOfWeek()

//    fun getSubjectsCount():Flow<Int> = subjectLocalDataSource.getSubjectsCount()
}