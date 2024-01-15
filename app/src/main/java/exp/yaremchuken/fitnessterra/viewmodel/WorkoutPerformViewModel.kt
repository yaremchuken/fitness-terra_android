package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import javax.inject.Inject

@HiltViewModel
class WorkoutPerformViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
): ViewModel() {
    fun getWorkout(id: Long) = workoutRepository.getById(id)
}