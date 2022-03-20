package uz.techie.schedulize.db

import kotlinx.coroutines.flow.Flow
import uz.techie.schedulize.db.models.DBSubjectModel
import uz.techie.schedulize.repositories.SubjectLocalDataSource

class RoomSubjectDataSource(private val subjectsListDao: SubjectsListDAO) : SubjectLocalDataSource {
    override fun getAllSubjects(): Flow<List<DBSubjectModel>> = subjectsListDao.getAllSubjects()

    override fun getAllSubjectsOfDay(day: String): Flow<List<DBSubjectModel>> =
        subjectsListDao.getAllSubjectsOfDay(day)

    override fun getSubject(id: Int): Flow<DBSubjectModel> = subjectsListDao.getSubjectById(id)

    override fun getDaysOfWeek(): Flow<List<String>> = subjectsListDao.getDaysOfWeek()

    override suspend fun insertSubject(subject: DBSubjectModel) {
        subjectsListDao.insert(subject)
    }

    override suspend fun insertAllSubjects(subjects: List<DBSubjectModel>) {
        subjectsListDao.insertAll(subjects)
    }

    override suspend fun deleteSubject(subject: DBSubjectModel) {
        subjectsListDao.delete(subject)
    }

    override suspend fun updateSubject(subject: DBSubjectModel) {
        subjectsListDao.update(subject)
    }


}