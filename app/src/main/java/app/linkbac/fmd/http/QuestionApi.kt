package app.linkbac.fmd.http

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*

class QuestionApi(private val client: HttpClient) {

    suspend fun getQuestions(): List<QuestionEntity> {
        return client.get("https://storeimg.com/fmdaily/api.php").body()
    }
}