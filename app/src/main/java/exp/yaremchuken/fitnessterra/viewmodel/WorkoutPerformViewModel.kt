package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntityWrapper
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import exp.yaremchuken.fitnessterra.data.model.ExerciseSwitchType
import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.service.TextToSpeechHelper
import exp.yaremchuken.fitnessterra.ui.view.perform.NextExerciseDto
import exp.yaremchuken.fitnessterra.ui.view.perform.WorkoutPerformState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject
import kotlin.time.Duration

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
        setIdx: Int,
        sideSwitched: Boolean
    ): NextExerciseDto {
        val section = workout.sections[sectionIdx]
        val setup = section.setups[setupIdx]

        if (setup.exercise.sideSwitchType == ExerciseSwitchType.SIDE_SWITCH_ON_SET && !sideSwitched) {
            return NextExerciseDto(
                workout.sections[sectionIdx],
                workout.sections[sectionIdx].setups[setupIdx].exercise,
                setupIdx,
                setIdx,
                true
            )
        }

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
            false,
            newSectionIdx != sectionIdx,
            newSetupIdx != setupIdx
        )
    }

    fun initSpeak() {
        textToSpeechHelper.hold = false
    }

    fun speakWorkoutBegin(template: String, sideSwitchTxt: String, workout: Workout) {
        val section = workout.sections[0]
        val setup = section.setups[0]
        textToSpeechHelper.speakOut(
            template.replace(":workout", workout.title)
                .replace(":section", section.title)
                .replace(":exercises", "${section.setups.size}")
                .replace(":exercise", setup.exercise.title)
                .replace(":sets", "${setup.sets.size}")
                .replace(":set", "${setup.sets[0]}")
                .replace(
                    ":side",
                    if (setup.exercise.sideSwitchType != ExerciseSwitchType.SIDE_SWITCH_ON_SET) ""
                    else sideSwitchTxt
                )
        )
    }

    fun speakOut(text: String) {
        textToSpeechHelper.speakOut(text)
    }

    fun stopSpeak() {
        textToSpeechHelper.stop()
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

    fun isBeforeSideSwitched(setup: ExerciseSetup, sideSwitched: Boolean) =
        setup.exercise.sideSwitchType == ExerciseSwitchType.SIDE_SWITCH_ON_SET && !sideSwitched

    /**
     * Recovery time between sets and exercises.
     * After final set recovery time is 4x times more that between sets.
     */
    fun getRecoveryAfterCompleteExercise(setup: ExerciseSetup, setIdx: Int, sideSwitched: Boolean) =
        setup.recovery *
                if (setIdx == setup.sets.size-1) {
                    if (isBeforeSideSwitched(setup, sideSwitched)) 1 else 4
                }
                else 1

    fun getBottomBlockWeight(state: WorkoutPerformState) =
        when(state) {
            WorkoutPerformState.RECOVERY -> 2F
            else -> 1F
        }
}