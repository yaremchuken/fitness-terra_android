package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntityWrapper
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import javax.inject.Inject

@HiltViewModel
class WorkoutLibraryViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {
    fun getWorkouts() = workoutRepository.getAll()

    fun fromEntity(entity: WorkoutEntityWrapper) =
        WorkoutRepository.fromEntity(
            entity,
            exerciseRepository.getAll()
        )
}