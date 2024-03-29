package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntityWrapper
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceEntityWrapper
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.data.model.TimedWorkout
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutSequenceRepository
import exp.yaremchuken.fitnessterra.toLocalDate
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ScheduleCalendarViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val workoutSequenceRepository: WorkoutSequenceRepository,
    private val exerciseRepository: ExerciseRepository,
    private val historyRepository: HistoryRepository
): ViewModel() {
    companion object {
        const val DAYS_IN_WEEK = 7
        // Calendar should show 6 nearest weeks
        const val WEEKS_IN_CALENDAR = 6
    }

    fun getSchedules(from: LocalDate, to: LocalDate) =
        scheduleRepository.getInPeriod(from, to, weekdaysInPeriod(from, to))

    fun fromEntity(entity: ScheduleEntityWrapper) =
        scheduleRepository.fromEntity(entity, exerciseRepository.getAll())

    fun getHistories(from: LocalDate, to: LocalDate) = historyRepository.getInPeriod(from, to)

    fun fromEntity(entity: HistoryEntity) =
        HistoryRepository.fromEntity(entity, exerciseRepository.getAll())

    fun getSequences() = workoutSequenceRepository.getAll()

    fun fromEntity(entity: WorkoutSequenceEntityWrapper) =
        WorkoutSequenceRepository.fromEntity(entity, exerciseRepository.getAll())

    fun getDatesForMonth(yearMonth: YearMonth): List<LocalDate> {
        val firstDay = yearMonth.atDay(1)
        val firstDayOffset = -firstDay.dayOfWeek.value + 1
        val dates: MutableList<LocalDate> = mutableListOf()
        for (i in firstDayOffset until (DAYS_IN_WEEK * WEEKS_IN_CALENDAR - firstDayOffset)) {
            dates.add(firstDay.plusDays(i.toLong()))
        }
        return dates.toList()
    }

    fun getWorkoutsForDate(onDate: LocalDate, schedules: List<Schedule>, timed: List<TimedWorkout>?): List<Workout> {
        val onDateScheduled = schedules
                .filter { schedule ->
                    (schedule.scheduledAt.toLocalDate() == onDate && schedule.weekdays.isEmpty()) ||
                    (onDate >= LocalDate.now() && schedule.weekdays.contains(onDate.dayOfWeek))
                }
                .map { it.workout }

        val onDateSequences = timed.orEmpty().map { it.workout }

        return onDateScheduled.plus(onDateSequences)
    }

    private fun weekdaysInPeriod(from: LocalDate, to: LocalDate): List<DayOfWeek> =
        generateSequence(from) { it.plusDays(1) }
            .takeWhile { it <= to }
            .map { it.dayOfWeek }
            .distinct()
            .toList()
}