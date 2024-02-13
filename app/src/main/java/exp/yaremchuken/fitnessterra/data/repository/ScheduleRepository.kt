package exp.yaremchuken.fitnessterra.data.repository

import exp.yaremchuken.fitnessterra.data.dao.ScheduleDao
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntityWrapper
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.toInstant
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate

class ScheduleRepository(
    private val dao: ScheduleDao
) {
    suspend fun insert(schedule: Schedule) = dao.insert(toEntity(schedule))

    suspend fun delete(schedule: Schedule) = dao.delete(toEntity(schedule))

    fun getOnDate(date: LocalDate) = getInPeriod(date, date, listOf(date.dayOfWeek))

    fun getInPeriod(from: LocalDate, to: LocalDate, weekdays: List<DayOfWeek>) =
        dao.getInPeriod(
            from.toInstant().toEpochMilli(),
            to.plusDays(1).toInstant().toEpochMilli(),
            mon = weekdays.contains(DayOfWeek.MONDAY),
            tue = weekdays.contains(DayOfWeek.TUESDAY),
            wed = weekdays.contains(DayOfWeek.WEDNESDAY),
            thu = weekdays.contains(DayOfWeek.THURSDAY),
            fri = weekdays.contains(DayOfWeek.FRIDAY),
            sat = weekdays.contains(DayOfWeek.SATURDAY),
            sun = weekdays.contains(DayOfWeek.SUNDAY)
        )

    private fun toEntity(schedule: Schedule) =
        ScheduleEntity(
            scheduledAt = schedule.scheduledAt.toEpochMilli(),
            workoutId = schedule.workout.id,
            monday = schedule.weekdays.contains(DayOfWeek.MONDAY),
            tuesday = schedule.weekdays.contains(DayOfWeek.TUESDAY),
            wednesday = schedule.weekdays.contains(DayOfWeek.WEDNESDAY),
            thursday = schedule.weekdays.contains(DayOfWeek.THURSDAY),
            friday = schedule.weekdays.contains(DayOfWeek.FRIDAY),
            saturday = schedule.weekdays.contains(DayOfWeek.SATURDAY),
            sunday = schedule.weekdays.contains(DayOfWeek.SUNDAY)
        )

    fun fromEntity(entity: ScheduleEntityWrapper, exercises: List<Exercise>) =
        Schedule(
            scheduledAt = Instant.ofEpochMilli(entity.scheduleEntity.scheduledAt),
            WorkoutRepository.fromEntity(entity.workout, exercises),
            convertWeekdays(
                listOf(
                    entity.scheduleEntity.monday,
                    entity.scheduleEntity.tuesday,
                    entity.scheduleEntity.wednesday,
                    entity.scheduleEntity.thursday,
                    entity.scheduleEntity.friday,
                    entity.scheduleEntity.saturday,
                    entity.scheduleEntity.sunday
                )
            )
        )

    private fun convertWeekdays(dayFlags: List<Boolean>): List<DayOfWeek> {
        val weekdays = ArrayList<DayOfWeek>()
        dayFlags.forEachIndexed { i, flag ->
            if (flag) {
                weekdays.add(DayOfWeek.of(i + 1))
            }
        }
        return weekdays
    }
}