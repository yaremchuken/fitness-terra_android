package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntityWrapper
import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.service.TextToSpeechHelper
import exp.yaremchuken.fitnessterra.ui.view.perform.NextExerciseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class WorkoutPerformViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: HistoryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val textToSpeechHelper: TextToSpeechHelper
): ViewModel() {

    private var startedAt: Instant? = null

    fun getWorkout(id: Long) = workoutRepository.getById(id)

    fun fromEntity(entity: WorkoutEntityWrapper) =
        WorkoutRepository.fromEntity(
            entity,
            exerciseRepository.getAll()
        )

    fun markStart() {
        if (startedAt == null) {
            startedAt = Instant.now()
        }
    }

    fun isStarted() = startedAt != null

    fun getNextExerciseDto(
        workout: Workout,
        sectionIdx: Int,
        setupIdx: Int,
        setIdx: Int
    ): NextExerciseDto {
        val section = workout.sections[sectionIdx]
        val setup = section.setups[setupIdx]

        var newSectionIdx = sectionIdx
        var newSetupIdx = setupIdx
        var newSetIdx = setIdx + 1

        if (setIdx + 1 >= setup.sets.size) {
            newSetIdx = 0
            newSetupIdx++
            if (setupIdx + 1 >= section.setups.size) {
                newSetupIdx = 0
                newSectionIdx++
                if (sectionIdx + 1 >= workout.sections.size) {
                    throw IllegalArgumentException("Workout section is finished!")
                }
            }
        }

        return NextExerciseDto(
            workout.sections[newSectionIdx],
            workout.sections[newSectionIdx].setups[newSetupIdx].exercise,
            newSetupIdx,
            newSetIdx,
            newSectionIdx != sectionIdx,
            newSetupIdx != setupIdx
        )
    }

    fun initSpeak() {
        textToSpeechHelper.hold = false
    }

    fun speakWorkoutBegin(template: String, workout: Workout) {
        val section = workout.sections[0]
        val setup = section.setups[0]
        textToSpeechHelper.speakOut(
            template.replace(":workout", workout.title)
                .replace(":section", section.title)
                .replace(":exercises", "${section.setups.size}")
                .replace(":exercise", setup.exercise.title)
                .replace(":sets", "${setup.sets.size}")
                .replace(":set", "${setup.sets[0]}")
        )
    }

    fun speakOut(text: String) {
        textToSpeechHelper.speakOut(text)
    }

    fun clearSpeak() {
        textToSpeechHelper.clear()
    }

    fun persistHistory(workout: Workout) {
        viewModelScope.launch (Dispatchers.IO){
            historyRepository.insert(
                History(
                    startedAt = startedAt!!,
                    finishedAt = Instant.now(),
                    workout = workout
                )
            )
        }
    }
}