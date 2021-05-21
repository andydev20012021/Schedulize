package uz.techie.schedulize.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.techie.schedulize.models.Subject
import java.time.DayOfWeek

@Dao
interface SubjectsListDAO {
    @Query("Select * From subject")
    fun getAllSubjects():Flow<List<Subject>>

    @Query("Select * From ${Subject.TABLE_NAME} where dayOfWeek=(:day)")
    fun getAllSubjectsOfDay(day:Int):Flow<List<Subject>>

    @Query("Select * From ${Subject.TABLE_NAME} where id=(:id)")
    fun getSubjectById(id:Int):Flow<Subject>

    @Query("Select dayOfWeek from ${Subject.TABLE_NAME} group by dayOfWeek")
    fun getDaysOfWeek():Flow<List<Int>>
//
//    @Query("Select count(id) from ${Subject.TABLE_NAME} ")
//    fun getSubjectsCount():Flow<Int>

    @Insert(entity = Subject::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subject: Subject)

    @Update(entity = Subject::class)
    suspend fun update(subject: Subject)

    @Delete(entity = Subject::class)
    suspend fun delete(subject: Subject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(subjects:List<Subject>)

    @Query("Delete From ${Subject.TABLE_NAME}")
    suspend fun deleteAll()

}