package app.linkbac.fmd.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Question(
    @PrimaryKey val uid: Int,
    val question: String,
    val answer: String,
    val topic: String,
    val difficulty: String,
    val examStyle: Int,
    val marks: Int,
    val createdAt: String,
    val forDay: String? = null,
    val attempted: Boolean = false,
    val correct: Boolean = false,
)