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

            template.id = schedule.id
            template.workout = schedule.workout
            template.weekdays = schedule.weekdays.toMutableList()

            return template
        }
    }

    var id: Long? = null
    var workout: Workout? = null
    var weekdays: MutableList<DayOfWeek> = mutableListOf()

    fun withId(id: Long): ScheduleTemplate {
        this.id = id
        return this
    }

    fun withWorkout(workout: Workout): ScheduleTemplate {
        this.workout = workout
        return this
    }

    fun withWeekdays(weekdays: MutableList<DayOfWeek>): ScheduleTemplate {
        this.weekdays = weekdays
        return this
    }

    fun toSchedule() = Schedule(id, scheduledAt, workout!!, weekdays)

    fun copy(): ScheduleTemplate {
        val copy = ScheduleTemplate(scheduledAt)
        copy.id = id
        copy.workout = workout
        copy.weekdays = weekdays.toMutableList()
        return copy
    }
}