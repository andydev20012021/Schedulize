package uz.techie.schedulize.utils.utilModel

import kotlinx.serialization.Serializable
import uz.techie.schedulize.db.models.DBSubjectModel

@Serializable
data class Subject(
    val subjectName: String,
    val subjectTeacher: String,
    val subjectPlace: String,
    val subjectPeriod: String,
    val dayOfWeek: String,
    val colorName: String
)

fun Subject.mapToDBSubjectModel(): DBSubjectModel{
    return DBSubjectModel(
        id = null,
        subjectName = subjectName,
        subjectTeacher = subjectTeacher,
        subjectPlace = subjectPlace,
        subjectPeriod = subjectPeriod,
        dayOfWeek = dayOfWeek,
        colorName = colorName
    )
}

fun DBSubjectModel.mapToUtilSubjectModel(): Subject{
    return Subject(
        subjectName = subjectName,
        subjectTeacher = subjectTeacher,
        subjectPlace = subjectPlace,
        subjectPeriod = subjectPeriod,
        dayOfWeek = dayOfWeek,
        colorName = colorName
    )
}
