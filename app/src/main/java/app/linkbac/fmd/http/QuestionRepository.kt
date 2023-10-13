package app.linkbac.fmd.http

interface QuestionRepository {
    suspend fun getQuestions(): List<QuestionEntity>
}