package app.linkbac.fmd.http

class QuestionRepositoryImpl(private val api: QuestionApi) : QuestionRepository {

    override suspend fun getQuestions(): List<QuestionEntity> {
        return api.getQuestions()
    }
}