package app.linkbac.fmd.http

interface QuestionRepository {
    suspend fun getQuestions(seenQuestionIDs: List<Int>): List<QuestionEntity>
}