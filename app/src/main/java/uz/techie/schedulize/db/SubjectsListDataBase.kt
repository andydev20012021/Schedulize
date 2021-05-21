package uz.techie.schedulize.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uz.techie.schedulize.models.Subject

@Database(entities = [Subject::class],version = 1)
@TypeConverters(SubjectTypeConverters::class)
open abstract class SubjectsListDataBase:RoomDatabase() {
    abstract fun subjectDAO():SubjectsListDAO
}