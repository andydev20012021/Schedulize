package uz.techie.schedulize.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.techie.schedulize.db.models.DBSubjectModel

@Dao
interface SubjectsListDAO {
    @Query("Select * From subject")
    fun getAllSubjects(): Flow<List<DBSubjectModel>>

    @Query("Select * From ${DBSubjectModel.TABLE_NAME} where dayOfWeek=(:day)")
    fun getAllSubjectsOfDay(day: String): Flow<List<DBSubjectModel>>

    @Query("Select * From ${DBSubjectModel.TABLE_NAME} where id=(:id)")
    fun getSubjectById(id: Int): Flow<DBSubjectModel>

    @Query("Select dayOfWeek from ${DBSubjectModel.TABLE_NAME} group by dayOfWeek")
    fun getDaysOfWeek(): Flow<List<String>>

    @Insert(entity = DBSubjectModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subject: DBSubjectModel)

    @Update(entity = DBSubjectModel::class)
    suspend fun update(subject: DBSubjectModel)

    @Delete(entity = DBSubjectModel::class)
    suspend fun delete(subject: DBSubjectModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subjects: List<DBSubjectModel>)

    @Query("Delete From ${DBSubjectModel.TABLE_NAME}")
    suspend fun deleteAll()

}