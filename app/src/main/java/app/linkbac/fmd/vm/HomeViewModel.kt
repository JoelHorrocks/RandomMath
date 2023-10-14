package app.linkbac.fmd.vm

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linkbac.fmd.http.QuestionApi
import app.linkbac.fmd.http.QuestionRepositoryImpl
import app.linkbac.fmd.http.ktorHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state get() = _state.value

    enum class ContentType {
        Latex, Text
    }

    fun getQuestions() {
        _state.value = state.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            QuestionRepositoryImpl(QuestionApi(ktorHttpClient)).getQuestions().let { questions ->
                viewModelScope.launch(Dispatchers.Main) {
                    _state.value = state.copy(isLoading = false, questions = questions)
                }
            }
        }
    }
}