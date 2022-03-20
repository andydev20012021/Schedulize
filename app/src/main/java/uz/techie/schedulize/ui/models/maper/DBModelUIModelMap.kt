package uz.techie.schedulize.ui.models.maper

import uz.techie.schedulize.db.models.DBSubjectModel
import uz.techie.schedulize.ui.models.CardColor
import uz.techie.schedulize.ui.models.DayOfWeek
import uz.techie.schedulize.ui.models.SubjectModel
import uz.techie.schedulize.ui.models.SubjectPeriod

fun DBSubjectModel.mapToUIModel(): SubjectModel {
    return SubjectModel(
        id = id,
        subjectName = subjectName,
        subjectPlace = subjectPlace,
        subjectTeacher = subjectTeacher,
        subjectPeriod = SubjectPeriod.valueOf(subjectPeriod),
        dayOfWeek = DayOfWeek.findByName(dayOfWeek),
        color = CardColor.findByColorName(colorName)
    )
}

fun SubjectModel.mapToDBSubjectModel(): DBSubjectModel {
    return DBSubjectModel(
        id = id,
        subjectName = subjectName,
        subjectTeacher = subjectTeacher,
        subjectPlace = subjectPlace,
        subjectPeriod = subjectPeriod.toString(),
        dayOfWeek = dayOfWeek.dayName,
        colorName = color.colorName
    )
}