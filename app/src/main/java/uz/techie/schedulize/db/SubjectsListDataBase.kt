package uz.techie.schedulize.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.techie.schedulize.db.models.DBSubjectModel

@Database(entities = [DBSubjectModel::class],version = 1)
abstract class SubjectsListDataBase:RoomDatabase() {
    abstract fun subjectDAO():SubjectsListDAO
}