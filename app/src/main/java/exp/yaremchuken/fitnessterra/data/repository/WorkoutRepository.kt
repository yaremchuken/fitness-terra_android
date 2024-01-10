package exp.yaremchuken.fitnessterra.data.repository

import exp.yaremchuken.fitnessterra.data.dao.WorkoutDao
import exp.yaremchuken.fitnessterra.data.model.Workout

class WorkoutRepository(
    private val dao: WorkoutDao
) {
    fun getById(id: Long): Workout? = dao.getById(id)
    fun getAll(): List<Workout> = dao.getAll()
}