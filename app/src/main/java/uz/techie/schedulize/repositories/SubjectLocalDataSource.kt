package uz.techie.schedulize.repositories

import kotlinx.coroutines.flow.Flow
import uz.techie.schedulize.db.models.DBSubjectModel

interface SubjectLocalDataSource {
    fun getAllSubjects(): Flow<List<DBSubjectModel>>
    fun getAllSubjectsOfDay(dayOfWeek: String): Flow<List<DBSubjectModel>>
    fun getSubject(id: Int): Flow<DBSubjectModel>
    fun getDaysOfWeek(): Flow<List<String>>
    suspend fun insertSubject(subject: DBSubjectModel)
    suspend fun insertAllSubjects(subjects: List<DBSubjectModel>)
    suspend fun deleteSubject(subject: DBSubjectModel)
    suspend fun updateSubject(subject: DBSubjectModel)
}