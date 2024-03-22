package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceEntityWrapper
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutSequenceRepository
import javax.inject.Inject

@HiltViewModel
class WorkoutSequencerViewModel @Inject constructor(
    private val workoutSequenceRepository: WorkoutSequenceRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {

    fun getSequences() = workoutSequenceRepository.getAll()

    fun fromEntity(entity: WorkoutSequenceEntityWrapper) =
        WorkoutSequenceRepository.fromEntity(
            entity,
            exerciseRepository.getAll()
        )
}