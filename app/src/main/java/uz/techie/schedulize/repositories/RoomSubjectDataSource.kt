package uz.techie.schedulize.repositories

import kotlinx.coroutines.flow.Flow
import uz.techie.schedulize.db.SubjectsListDAO
import uz.techie.schedulize.models.Subject

class RoomSubjectDataSource(private val subjectsListDao: SubjectsListDAO) : SubjectLocalDataSource {
    override fun getAllSubjects(): Flow<List<Subject>> = subjectsListDao.getAllSubjects()

    override fun getAllSubjectsOfDay(day: Int): Flow<List<Subject>> =
        subjectsListDao.getAllSubjectsOfDay(day)

    override fun getSubject(id: Int): Flow<Subject> = subjectsListDao.getSubjectById(id)

    override fun getDaysOfWeek(): Flow<List<Int>> = subjectsListDao.getDaysOfWeek()

//    override fun getSubjectsCount(): Flow<Int> = subjectsListDao.getSubjectsCount()

    override suspend fun insertSubject(subject: Subject) {
        subjectsListDao.insert(subject)
    }

    override suspend fun deleteSubject(subject: Subject) {
        subjectsListDao.delete(subject)
    }

    override suspend fun updateSubject(subject: Subject) {
        subjectsListDao.update(subject)
    }


}