package app.linkbac.fmd.http

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionEntity(
    @SerialName("question_id")
    val questionID: Int,
    @SerialName("question_text")
    val questionText: String,
    @SerialName("answer_text")
    val answerText: String,
    @SerialName("topic")
    val topic: String,
    @SerialName("difficulty")
    val difficulty: String,
    @SerialName("exam_style")
    val examStyle: Int,
    @SerialName("marks")
    val marks: Int,
    @SerialName("created_at")
    val createdAt: String,
)
