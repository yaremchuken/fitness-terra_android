package exp.yaremchuken.fitnessterra.data.datasource

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import exp.yaremchuken.fitnessterra.AppSettings
import exp.yaremchuken.fitnessterra.data.datasource.dto.ExerciseDto
import exp.yaremchuken.fitnessterra.data.datasource.dto.WorkoutDto
import exp.yaremchuken.fitnessterra.data.model.Exercise
import exp.yaremchuken.fitnessterra.data.model.ExerciseSet
import exp.yaremchuken.fitnessterra.data.model.Workout
import exp.yaremchuken.fitnessterra.data.model.WorkoutSection
import kotlin.time.Duration.Companion.milliseconds

/**
 * Datasource for some pre-defined data from assets in yaml format.
 * It's readonly of course.
 */
object YamlDatasource {

    lateinit var exercises: List<Exercise>
    lateinit var workouts: List<Workout>

    fun preload(context: Context) {
        val mapper = ObjectMapper(YAMLFactory())

        exercises =
            context.assets
                .open("datasource/exercise.yaml")
                .use { mapper.readValue(it, ExerciseSource::class.java) }
                .entities
                .map {
                    Exercise(
                        it.id,
                        it.title[AppSettings.locale()]!!,
                        it.description[AppSettings.locale()]!!,
                        it.muscleGroup,
                        it.muscles,
                        it.equipment,
                        it.steps[AppSettings.locale()]!!,
                        it.advises[AppSettings.locale()] ?: listOf(),
                        it.warnings[AppSettings.locale()] ?: listOf(),
                        it.repeatTime.milliseconds,
                        it.recovery.milliseconds
                    )
                }

        workouts =
            context.assets
                .open("datasource/workout.yaml")
                .use { mapper.readValue(it, WorkoutSource::class.java) }
                .entities
                .map {
                    Workout(
                        it.id,
                        it.title[AppSettings.locale()]!!,
                        it.type,
                        it.sections.map { sec ->
                            WorkoutSection(
                                sec.title[AppSettings.locale()]!!,
                                sec.sets.map { set ->
                                    ExerciseSet(
                                        exercises.find { ex -> ex.id == set.exerciseId }!!,
                                        set.weight,
                                        set.repeats,
                                        set.duration.milliseconds,
                                        set.recovery.milliseconds
                                    )
                                }
                            )
                        }
                    )
                }
    }
}

class ExerciseSource {
    lateinit var entities: List<ExerciseDto>
}

class WorkoutSource {
    lateinit var entities: List<WorkoutDto>
}