package app.linkbac.fmd.vm

import app.linkbac.fmd.http.QuestionEntity

data class HomeState(
    val isLoading: Boolean = false,
    val questions: List<QuestionEntity> = emptyList(),
    val error: String = ""
)