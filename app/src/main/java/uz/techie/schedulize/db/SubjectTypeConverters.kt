package uz.techie.schedulize.db

import androidx.room.TypeConverter
import uz.techie.schedulize.utils.SubjectPeriod

open class SubjectTypeConverters {
    @TypeConverter
    fun fromSubjectPeriod(subjectPeriod: SubjectPeriod):String =
        subjectPeriod.start.toString() + "/" + subjectPeriod.end.toString()

    @TypeConverter
    fun toSubjectPeriod(period:String):SubjectPeriod = SubjectPeriod.valueOf(period)
}