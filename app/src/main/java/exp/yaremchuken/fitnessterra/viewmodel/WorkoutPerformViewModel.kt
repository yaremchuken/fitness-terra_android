package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.ui.view.perform.NextExerciseDto
import javax.inject.Inject

@HiltViewModel
class WorkoutPerformViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository
): ViewModel() {
    fun getWorkout(id: Long) = workoutRepository.getById(id)

    fun getNextExerciseDto(
        workout: Workout,
        sectionIdx: Int,
        setIdx: Int,
        repeatIdx: Int
    ): NextExerciseDto {
        val section = workout.sections[sectionIdx]
        val set = section.sets[setIdx]

        var newSectionIdx = sectionIdx
        var newSetIdx = setIdx
        var newRepeatIdx = repeatIdx + 1

        if (repeatIdx + 1 >= set.repeats.size) {
            newRepeatIdx = 0
            newSetIdx++
            if (setIdx + 1 >= section.sets.size) {
                newSetIdx = 0
                newSectionIdx++
                if (sectionIdx + 1 >= workout.sections.size) {
                    throw IllegalArgumentException("Workout section is finished!")
                }
            }
        }

        return NextExerciseDto(
            workout.sections[newSectionIdx],
            workout.sections[newSectionIdx].sets[newSetIdx].exercise,
            newSetIdx,
            newRepeatIdx
        )
    }

    fun persistHistory(workout: Workout) {
        throw NotImplementedError("Persist Workout: ${workout.id}")
    }
}