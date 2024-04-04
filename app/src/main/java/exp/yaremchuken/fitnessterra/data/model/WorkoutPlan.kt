package exp.yaremchuken.fitnessterra.data.model

/**
 * Workout plan is an order in which sections and exercises should be performed.
 * Mostly used if workout had been shuffled.
 */
data class WorkoutPlan(
    /**
     * Scheme looks like
     *  orders:
     *      - sectionId
     *          - setupId
     *          - setupId
     *      - sectionId
     *          - setupId
     */
    val orders: List<Pair<Long, List<Long>>>
) {
    companion object {
        fun convert(workout: Workout) =
            WorkoutPlan(
                workout.sections.map { sec ->
                    Pair(
                        sec.id,
                        sec.setups.map { set ->
                            set.exercise.id
                        }
                    )
                }
            )

        fun rebuild(workout: Workout, plan: WorkoutPlan) =
            Workout(
                workout.id,
                workout.title,
                workout.type,
                plan.orders.mapIndexed { secIdx, secOrd ->
                    val section = workout.sections.find { it.id == secOrd.first }!!
                    WorkoutSection(
                        section.id,
                        secIdx.toLong(),
                        section.title,
                        secOrd.second.mapIndexed { setIdx, setOrd ->
                            val setup = section.setups.find { it.exercise.id == setOrd }!!
                            ExerciseSetup(
                                section.id,
                                setup.exercise,
                                setIdx.toLong(),
                                setup.equipment,
                                setup.sets,
                                setup.duration,
                                setup.recovery
                            )
                        }
                    )
                }
            )
    }
}