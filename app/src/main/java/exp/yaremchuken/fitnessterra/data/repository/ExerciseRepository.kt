package exp.yaremchuken.fitnessterra.data.repository

import exp.yaremchuken.fitnessterra.data.dao.ExerciseDao
import exp.yaremchuken.fitnessterra.data.model.Exercise

class ExerciseRepository(
    private val dao: ExerciseDao
) {
    fun getByIds(ids: List<Long>): List<Exercise> = dao.getByIds(ids)

    fun getAll(): List<Exercise> = dao.getAll()
}