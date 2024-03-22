package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntityWrapper
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.ExerciseSetupRepository
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutSectionRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutSequenceRepository
import exp.yaremchuken.fitnessterra.service.TextToSpeechHelper
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val historyRepository: HistoryRepository,
    private val exerciseRepository: ExerciseRepository,
    private val exerciseSetupRepository: ExerciseSetupRepository,
    private val workoutRepository: WorkoutRepository,
    private val workoutSectionRepository: WorkoutSectionRepository,
    private val workoutSequenceRepository: WorkoutSequenceRepository,
    // greedy tts initialization, so it'll be ready when other vm gonna need it
    private val textToSpeechHelper: TextToSpeechHelper
): ViewModel() {

    fun getTodaySchedules() = scheduleRepository.getOnDate(LocalDate.now())

    fun fromEntity(entity: ScheduleEntityWrapper) = scheduleRepository.fromEntity(entity, exerciseRepository.getAll())

    fun getLatestHistory(limit: Long) = historyRepository.getLatest(limit)

    fun fromEntity(entity: HistoryEntity) = HistoryRepository.fromEntity(entity, exerciseRepository.getAll())
}