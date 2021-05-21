package uz.techie.schedulize.models

import androidx.annotation.ColorRes
import androidx.annotation.IntegerRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.techie.schedulize.utils.SubjectPeriod
import java.time.DayOfWeek

@Entity(tableName = Subject.TABLE_NAME)
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val subjectName:String,
    val subjectPeriod:SubjectPeriod,
    val subjectTeacher:String?,
    val subjectPlace:String?,
    val dayOfWeek: Int,
    @ColorRes val color:Int
){
    companion object{
        const val TABLE_NAME = "subject"
    }
}