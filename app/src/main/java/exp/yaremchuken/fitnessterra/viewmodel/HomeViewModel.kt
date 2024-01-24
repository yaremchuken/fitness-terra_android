package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.service.TextToSpeechHelper
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: HistoryRepository,
    // greedy tts initialization, so it'll be ready when other vm gonna need it
    private val textToSpeechHelper: TextToSpeechHelper
): ViewModel() {
    fun getTodaySchedules() = scheduleRepository.getOnDate(LocalDate.now())

    fun fromEntity(entity: ScheduleEntity) =
        scheduleRepository.fromEntity(entity, workoutRepository.getById(entity.workoutId)!!)

    fun getLatestHistory(limit: Long) = historyRepository.getLatest(limit)

    fun fromEntity(entity: HistoryEntity)=
        historyRepository.fromEntity(entity, workoutRepository.getById(entity.workoutId)!!)
}