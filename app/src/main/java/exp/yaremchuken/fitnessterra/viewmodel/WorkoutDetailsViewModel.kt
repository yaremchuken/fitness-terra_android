package exp.yaremchuken.fitnessterra.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntityWrapper
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.util.Utils
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: HistoryRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {
    fun getWorkout(id: Long) = workoutRepository.getById(id)

    fun fromEntity(entity: WorkoutEntityWrapper) =
        WorkoutRepository.fromEntity(
            entity,
            exerciseRepository.getAll()
        )

    fun getHistory(startedAt: Instant) = historyRepository.getByStartedAt(startedAt)

    fun fromEntity(entity: HistoryEntity) = HistoryRepository.fromEntity(entity, exerciseRepository.getAll())

    fun getPreview(workout: Workout, context: Context) = Utils.getWorkoutPreview(context, workout)

    fun equipment(workout: Workout) =
        workout.sections
            .asSequence()
            .map { it.setups }
            .flatten()
            .map { it.exercise.equipment.map { e -> e.type }.distinct() }
            .flatten()
            .distinct()
            .toList()
}