package app.linkbac.fmd.vm

import app.linkbac.fmd.ProcessedQuestion
import app.linkbac.fmd.db.Question

data class ProfileState(
    val isLoading: Boolean = false,
    val attemptedQuestions: List<ProcessedQuestion> = emptyList(),
    val error: String = ""
)