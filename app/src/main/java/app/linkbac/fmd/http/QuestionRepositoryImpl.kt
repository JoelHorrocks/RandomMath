package app.linkbac.fmd.http

class QuestionRepositoryImpl(private val api: QuestionApi) : QuestionRepository {

    override suspend fun getQuestions(seenQuestionIDs: List<Int>): ApiResponse<List<QuestionEntity>, Any> {
        return api.getQuestions(seenQuestionIDs)
    }
}