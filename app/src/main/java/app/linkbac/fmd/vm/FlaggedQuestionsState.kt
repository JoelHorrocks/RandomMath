package app.linkbac.fmd.vm

import app.linkbac.fmd.ProcessedQuestion
import app.linkbac.fmd.db.Question

data class FlaggedQuestionsState(
    val isLoading: Boolean = false,
    val flaggedQuestions: List<ProcessedQuestion> = emptyList(),
    val error: String = ""
)