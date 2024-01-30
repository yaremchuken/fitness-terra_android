package exp.yaremchuken.fitnessterra.ui.view.schedule.date

import exp.yaremchuken.fitnessterra.data.model.Schedule
import exp.yaremchuken.fitnessterra.data.model.Workout
import java.time.DayOfWeek
import java.time.Instant

class ScheduleTemplate(
    val scheduledAt: Instant
) {
    companion object {
        fun toTemplate(schedule: Schedule): ScheduleTemplate {
            val template = ScheduleTemplate(schedule.scheduledAt)

            template.isExists = true
            template.workout = schedule.workout
            template.weekdays = schedule.weekdays.toMutableList()

            return template
        }
    }

    var isExists: Boolean = false
    var workout: Workout? = null
    var weekdays: MutableList<DayOfWeek> = mutableListOf()

    fun withWorkout(workout: Workout): ScheduleTemplate {
        this.workout = workout
        return this
    }

    fun withWeekdays(weekdays: MutableList<DayOfWeek>): ScheduleTemplate {
        this.weekdays = weekdays
        return this
    }

    fun toSchedule() = Schedule(scheduledAt, workout!!, weekdays)
}