package exp.yaremchuken.fitnessterra.viewmodel

import exp.yaremchuken.fitnessterra.data.model.History
import exp.yaremchuken.fitnessterra.data.model.TimedWorkout
import exp.yaremchuken.fitnessterra.data.model.WorkoutSequence
import exp.yaremchuken.fitnessterra.toLocalDate
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
                .filter { h -> workouts.find { it.id == h.workout.id } != null }
                .maxByOrNull { it.finishedAt }

        var idxOffset =
            if (recentHistory == null) 0
            else if (workouts.indexOfFirst { it.id == recentHistory.workout.id }  == (workouts.size-1)) 0
            else workouts.indexOfFirst { it.id == recentHistory.workout.id } + 1

        var currDate = startDate

        if (histories
            .filter { it.finishedAt.toLocalDate() == LocalDate.now() }
            .any { workouts.indexOfFirst { w -> w.id == it.workout.id } != -1 }
        ) {
            currDate = currDate.plusDays(1)
        }

        while (currDate <= endDate) {
            if (sequence.weekdays.contains(currDate.dayOfWeek)) {
                sequencedDates[currDate] = TimedWorkout(sequence.scheduledAt, workouts[idxOffset])
                println("$currDate -> $idxOffset")
                idxOffset = if (idxOffset == workouts.size-1) 0 else (idxOffset + 1)
            }

            currDate = currDate.plusDays(1)
        }

        return sequencedDates
    }
}