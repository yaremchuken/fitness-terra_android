package exp.yaremchuken.fitnessterra.viewmodel

import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.TimedWorkout
import exp.yaremchuken.fitnessterra.data.model.WorkoutSequence
import java.time.LocalDate

object WorkoutSequenceHelper {
    // Amount of days from today, for which workout sequences should be calculated
    const val SEQUENCE_DEFAULT_PERIOD_FOR_DISPLAY = 31L

    fun getTimedWorkoutsFromToday(period: Long, sequences: List<WorkoutSequence>, histories: List<History>) =
        sequences
            .map { getTimedWorkouts(period, it, histories) }
            .asSequence()
            .flatMap { it.asSequence() }
            .groupBy( { it.key }, { it.value })

    private fun getTimedWorkouts(
        period: Long,
        sequence: WorkoutSequence,
        histories: List<History>
    ): Map<LocalDate, TimedWorkout> {
        val startDate = LocalDate.now()
        val endDate = startDate.plusDays(period)

        val sequencedDates: MutableMap<LocalDate, TimedWorkout> = mutableMapOf()

        val workouts = sequence.workouts

        val recentHistory =
            histories
                .filter { workouts.contains(it.workout) }
                .maxByOrNull { it.finishedAt }

        var idxOffset =
            if (recentHistory == null || workouts.indexOf(recentHistory.workout) == workouts.size) 0
            else (workouts.indexOf(recentHistory.workout) + 1)

        var currDate = startDate
        while (currDate <= endDate) {
            if (sequence.weekdays.contains(currDate.dayOfWeek)) {
                sequencedDates[currDate] = TimedWorkout(sequence.scheduledAt, workouts[idxOffset])
                idxOffset = if (idxOffset == workouts.size-1) 0 else (idxOffset + 1)
            }

            currDate = currDate.plusDays(1)
        }

        return sequencedDates
    }
}