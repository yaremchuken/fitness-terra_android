package exp.yaremchuken.fitnessterra.data.dao

import exp.yaremchuken.fitnessterra.data.datasource.YamlDatasource
import exp.yaremchuken.fitnessterra.data.model.Workout

class WorkoutDao(
    private val datasource: YamlDatasource
) {
    fun getById(id: Long): Workout? = datasource.workouts.find { it.id == id }?.copy()
    fun getAll(): List<Workout> = datasource.workouts.map { it.copy() }
}