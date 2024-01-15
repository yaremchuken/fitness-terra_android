package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
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
    private val workoutRepository: WorkoutRepository
): ViewModel() {
    fun getSchedules(onDate: LocalDate) = scheduleRepository.getAllInPeriod(onDate, onDate, listOf(onDate.dayOfWeek))

    fun insertSchedule(schedule: Schedule) {
        viewModelScope.launch (Dispatchers.IO){ scheduleRepository.insert(schedule) }
    }

    fun deleteSchedule(schedule: Schedule) {
        viewModelScope.launch (Dispatchers.IO){ scheduleRepository.delete(schedule) }
    }

    fun getWorkouts() = workoutRepository.getAll()

    fun fromEntity(entity: ScheduleEntity) = scheduleRepository.fromEntity(entity, workoutRepository)

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