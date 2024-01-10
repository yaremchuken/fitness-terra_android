package exp.yaremchuken.fitnessterra.data.repository

import exp.yaremchuken.fitnessterra.data.dao.ExerciseDao
import exp.yaremchuken.fitnessterra.data.model.Exercise

class ExerciseRepository(
    private val dao: ExerciseDao
) {
    fun getById(id: Long): Exercise? = dao.getById(id)
    fun getAll(): List<Exercise> = dao.getAll()
}