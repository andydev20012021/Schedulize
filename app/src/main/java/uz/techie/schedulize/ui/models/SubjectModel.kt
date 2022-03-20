package uz.techie.schedulize.ui.models

data class SubjectModel(
    val id: Int?,
    val subjectName: String,
    val subjectPeriod: SubjectPeriod,
    val subjectTeacher: String,
    val subjectPlace: String,
    val dayOfWeek: DayOfWeek,
    val color: CardColor,
)

val emptySubjectModel
    get() = SubjectModel(
        null,
        "",
        SubjectPeriod.valueOf("00:00-00:00"),
        "",
        "",
        DayOfWeek.SUNDAY,
        CardColor.DEFAULT
    )
