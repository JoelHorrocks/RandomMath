package app.linkbac.fmd.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuestionDao {
    @Query("SELECT * FROM question")
    fun getAll(): List<Question>

    @Query("SELECT * FROM question WHERE attempted = 0")
    fun getUnattempted(): List<Question>

    @Query("SELECT * FROM question WHERE uid IN (:questionIds)")
    fun loadAllByIds(questionIds: IntArray): List<Question>

    @Insert
    fun insertAll(vararg question: Question)

    @Delete
    fun delete(question: Question)

    @Update
    fun update(question: Question)

    @Query("DELETE FROM question WHERE attempted = 0")
    fun clearQueue()
}