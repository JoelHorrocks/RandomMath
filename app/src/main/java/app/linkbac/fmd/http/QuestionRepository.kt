package app.linkbac.fmd.http

interface QuestionRepository {
    suspend fun getQuestions(seenQuestionIDs: List<Int>): ApiResponse<List<QuestionEntity>, Any>
}