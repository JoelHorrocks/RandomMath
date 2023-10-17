package app.linkbac.fmd.vm

import app.linkbac.fmd.db.Question

data class HomeState(
    val isLoading: Boolean = false,
    val questions: List<Question> = emptyList(),
    val error: String = ""
)