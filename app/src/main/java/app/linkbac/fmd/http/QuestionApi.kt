package app.linkbac.fmd.http

import android.util.Log
import io.ktor.client.*
import io.ktor.http.URLProtocol
import io.ktor.http.path

class QuestionApi(private val client: HttpClient) {

    suspend fun getQuestions(seenQuestionIds: List<Int>): ApiResponse<List<QuestionEntity>, Any> {
        return client.safeRequest {
            this.url {
                protocol = URLProtocol.HTTPS
                host = "storeimg.com"
                path("fmdaily", "api-app.php")
                if(seenQuestionIds.isNotEmpty()) {
                    parameters.append("seenQuestionIDs", seenQuestionIds.joinToString(","))
                }
            }
        }
    }
}