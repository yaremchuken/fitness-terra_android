package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.ui.view.perform.NextExerciseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class WorkoutPerformViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: HistoryRepository
): ViewModel() {
    private var startedAt: Instant? = null

    fun markStart() {
        if (startedAt == null) {
            startedAt = Instant.now()
        }
    }

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
        viewModelScope.launch (Dispatchers.IO){
            historyRepository.insert(
                History(
                    id = null,
                    startedAt = startedAt!!,
                    finishedAt = Instant.now(),
                    workout = workout
                )
            )
        }
    }
}