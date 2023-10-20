package app.linkbac.fmd.vm

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linkbac.fmd.db.AppDatabase
import app.linkbac.fmd.db.Question
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScratchpadViewModel: ViewModel() {
    // TODO: handle this with a state so errors can be displayed
    private val _question: MutableState<Question?> = mutableStateOf(null)
    val question = _question

    fun loadQuestion(id: Int?, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ScratchpadViewModel", "Loading question with id $id")
            val question = AppDatabase.getInstance(context).questionDao().loadAllByIds(intArrayOf(id!!)).first()
            _question.value = question
        }
    }

    fun markQuestion(context: Context, result: Boolean) {
        _question.value = _question.value?.copy(attempted = true, correct = result)

        viewModelScope.launch(Dispatchers.IO) {
            AppDatabase.getInstance(context).questionDao().update(_question.value!!)
        }
    }
}