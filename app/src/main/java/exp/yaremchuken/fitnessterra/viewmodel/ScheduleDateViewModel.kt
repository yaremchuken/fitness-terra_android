package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntityWrapper
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntityWrapper
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceEntityWrapper
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutSequenceRepository
import exp.yaremchuken.fitnessterra.getHour
import exp.yaremchuken.fitnessterra.toInstant
import exp.yaremchuken.fitnessterra.ui.view.schedule.date.ScheduleTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ScheduleDateViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val workoutRepository: WorkoutRepository,
    private val workoutSequenceRepository: WorkoutSequenceRepository,
    private val historyRepository: HistoryRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {
    fun getSchedules(onDate: LocalDate) = scheduleRepository.getOnDate(onDate)

    fun insertSchedule(schedule: Schedule) {
        viewModelScope.launch (Dispatchers.IO){ scheduleRepository.insert(schedule) }
    }

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch (Dispatchers.IO){ scheduleRepository.delete(schedule) }
    }

    fun fromEntity(entity: ScheduleEntityWrapper) =
        scheduleRepository.fromEntity(entity, exerciseRepository.getAll())

    fun getHistories(from: LocalDate, to: LocalDate) = historyRepository.getInPeriod(from, to)

    fun fromEntity(entity: HistoryEntity) =
        HistoryRepository.fromEntity(entity, exerciseRepository.getAll())

    fun getWorkouts() = workoutRepository.getAll()

    fun fromEntity(entity: WorkoutEntityWrapper) = WorkoutRepository.fromEntity(entity, exerciseRepository.getAll())

    fun getSequences() = workoutSequenceRepository.getAll()

    fun fromEntity(entity: WorkoutSequenceEntityWrapper) =
        WorkoutSequenceRepository.fromEntity(entity, exerciseRepository.getAll())

    /**
     * Checks if a workout is already scheduled for this time.
     */
    fun getTemplate(schedules: List<Schedule>, date: LocalDate, hour: Int): ScheduleTemplate {
        val scheduled = schedules.firstOrNull { it.scheduledAt.getHour() == hour }
        return if (scheduled == null) ScheduleTemplate(atHour(date, hour))
        else ScheduleTemplate.toTemplate(scheduled)
    }

    private fun atHour(date: LocalDate, hour: Int) = date.toInstant().plus(hour.toLong(), ChronoUnit.HOURS)
}