package exp.yaremchuken.fitnessterra.data.repository

import exp.yaremchuken.fitnessterra.data.dao.WorkoutSectionDao
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSectionEntity

class WorkoutSectionRepository(
    private val dao: WorkoutSectionDao
) {
    suspend fun insert(entity: WorkoutSectionEntity) = dao.insert(entity)
}