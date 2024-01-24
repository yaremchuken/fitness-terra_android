package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class ScheduleCalendarViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val workoutRepository: WorkoutRepository,
    private val historyRepository: HistoryRepository
): ViewModel() {
    companion object {
        const val DAYS_IN_WEEK = 7
        // Calendar should show 6 nearest weeks
        const val WEEKS_IN_CALENDAR = 6
    }

    fun getSchedules(from: LocalDate, to: LocalDate) =
        scheduleRepository.getAllInPeriod(from, to, weekdaysInPeriod(from, to))

    fun fromEntity(entity: ScheduleEntity) =
        scheduleRepository.fromEntity(entity, workoutRepository.getById(entity.workoutId)!!)

    fun getHistories(from: LocalDate, to: LocalDate) = historyRepository.getInPeriod(from, to)

    fun fromEntity(entity: HistoryEntity) =
        historyRepository.fromEntity(entity, workoutRepository.getById(entity.workoutId)!!)

    fun getDatesForMonth(yearMonth: YearMonth): List<LocalDate> {
        val firstDay = yearMonth.atDay(1)
        val firstDayOffset = -firstDay.dayOfWeek.value + 1
        val dates: MutableList<LocalDate> = mutableListOf()
        for (i in firstDayOffset until (DAYS_IN_WEEK * WEEKS_IN_CALENDAR - firstDayOffset)) {
            dates.add(firstDay.plusDays(i.toLong()))
        }
        return dates.toList()
    }

    private fun weekdaysInPeriod(from: LocalDate, to: LocalDate): List<DayOfWeek> =
        generateSequence(from) { it.plusDays(1) }
            .takeWhile { it <= to }
            .map { it.dayOfWeek }
            .distinct()
            .toList()
}