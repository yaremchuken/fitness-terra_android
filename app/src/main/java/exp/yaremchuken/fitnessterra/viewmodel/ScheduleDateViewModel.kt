package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ScheduleDateViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val workoutRepository: WorkoutRepository
): ViewModel() {
    fun getSchedules(onDate: LocalDate) = scheduleRepository.getOnDate(onDate)

    fun getSchedules(weekdays: List<DayOfWeek>) = scheduleRepository.getWeekly(weekdays)

    fun insertSchedule(schedule: Schedule) = scheduleRepository.insert(schedule)

    fun deleteSchedule(schedule: Schedule) = scheduleRepository.delete(schedule)

    fun getWorkouts() = workoutRepository.getAll()

    fun fromEntity(entity: ScheduleEntity) = scheduleRepository.fromEntity(entity, workoutRepository)
}