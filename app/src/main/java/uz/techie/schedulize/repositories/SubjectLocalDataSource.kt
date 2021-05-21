package uz.techie.schedulize.repositories

import kotlinx.coroutines.flow.Flow
import uz.techie.schedulize.models.Subject
import java.time.DayOfWeek

interface SubjectLocalDataSource {
    fun getAllSubjects():Flow<List<Subject>>
    fun getAllSubjectsOfDay(dayOfWeek: Int):Flow<List<Subject>>
    fun getSubject(id:Int):Flow<Subject>
    fun getDaysOfWeek():Flow<List<Int>>
//    fun getSubjectsCount():Flow<Int>
    suspend fun insertSubject(subject: Subject)
    suspend fun deleteSubject(subject: Subject)
    suspend fun updateSubject(subject: Subject)
}