package app.linkbac.fmd.http

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*

class QuestionApi(private val client: HttpClient) {

    suspend fun getQuestions(seenQuestionIds: List<Int>): List<QuestionEntity> {
        val baseUrl = "https://storeimg.com/fmdaily/api-app.php"
        val url = if (seenQuestionIds.isEmpty()) {
            baseUrl
        } else {
            "$baseUrl?seenQuestionIDs=${seenQuestionIds.joinToString(",")}"
        }
        return client.get(url).body()
    }
}