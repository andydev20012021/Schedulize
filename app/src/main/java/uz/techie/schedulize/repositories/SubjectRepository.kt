package uz.techie.schedulize.repositories

import kotlinx.coroutines.flow.Flow
import uz.techie.schedulize.db.models.DBSubjectModel

class SubjectRepository(
    private val subjectLocalDataSource: SubjectLocalDataSource
) {
    suspend fun insertSubject(subject: DBSubjectModel) {
        subjectLocalDataSource.insertSubject(subject)
    }

    suspend fun insertAllSubjects(subjects: List<DBSubjectModel>){
        subjectLocalDataSource.insertAllSubjects(subjects)
    }

    suspend fun deleteSubject(subject: DBSubjectModel) {
        subjectLocalDataSource.deleteSubject(subject)
    }

    suspend fun updateSubject(subject: DBSubjectModel) {
        subjectLocalDataSource.updateSubject(subject)
    }

    fun getAllSubjectsOfDay(dayOfWeek: String): Flow<List<DBSubjectModel>> =
        subjectLocalDataSource.getAllSubjectsOfDay(dayOfWeek)

    fun getAllSubjects(): Flow<List<DBSubjectModel>> = subjectLocalDataSource.getAllSubjects()

    fun getSubjectById(id: Int): Flow<DBSubjectModel> = subjectLocalDataSource.getSubject(id)

    fun getDaysOfWeek(): Flow<List<String>> = subjectLocalDataSource.getDaysOfWeek()
}