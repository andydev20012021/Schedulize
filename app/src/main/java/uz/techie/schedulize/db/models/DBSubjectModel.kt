package uz.techie.schedulize.db.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Entity(tableName = DBSubjectModel.TABLE_NAME)
data class DBSubjectModel(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val subjectName: String,
    val subjectTeacher: String,
    val subjectPlace: String,
    val subjectPeriod: String, // 00:00-23:59
    val dayOfWeek: String,
    val colorName: String
) {
    companion object {
        const val TABLE_NAME = "subject"
    }
}
