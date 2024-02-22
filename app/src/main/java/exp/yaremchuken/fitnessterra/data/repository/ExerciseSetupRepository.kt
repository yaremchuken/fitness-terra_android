package exp.yaremchuken.fitnessterra.data.repository

import com.google.gson.Gson
import exp.yaremchuken.fitnessterra.data.dao.ExerciseSetupDao
import exp.yaremchuken.fitnessterra.data.entity.ExerciseSetupEntity
import exp.yaremchuken.fitnessterra.data.model.Equipment
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import kotlin.time.Duration.Companion.seconds

class ExerciseSetupRepository(
    private val dao: ExerciseSetupDao
) {
    suspend fun insert(setup: ExerciseSetup) = dao.insert(toEntity(setup))

    fun getBySectionAndExercise(sectionId: Long, exerciseId: Long) = dao.getBySectionAndExercise(sectionId, exerciseId)

    companion object {
        fun toEntity(setup: ExerciseSetup) =
            ExerciseSetupEntity(
                setup.sectionId,
                setup.exercise.id,
                setup.order,
                Gson().toJson(setup.equipment),
                Gson().toJson(setup.sets),
                setup.duration.inWholeSeconds,
                setup.recovery.inWholeSeconds
            )

        fun fromEntity(entity: ExerciseSetupEntity, sectionId: Long, exercise: Exercise) =
            ExerciseSetup(
                sectionId,
                exercise,
                entity.order,
                Gson().fromJson(entity.equipment, Array<Equipment>::class.java).asList(),
                Gson().fromJson(entity.sets, Array<Long>::class.java).asList(),
                entity.duration.seconds,
                entity.recovery.seconds
            )
    }
}