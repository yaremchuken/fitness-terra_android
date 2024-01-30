package exp.yaremchuken.fitnessterra.data.datasource

import android.app.Application
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import exp.yaremchuken.fitnessterra.data.datasource.dto.ExerciseDto
import exp.yaremchuken.fitnessterra.data.model.Exercise

/**
 * Datasource for some pre-defined data from assets in yaml format.
 * It's readonly of course.
 */
class YamlDatasource(
    app: Application
) {
    var exercises: List<Exercise>

    init {
        val mapper = ObjectMapper(YAMLFactory())

        exercises =
            app.assets
                .open("datasource/exercise.yaml")
                .use { mapper.readValue(it, ExerciseSource::class.java) }
                .entities
                .map { ExerciseDto.fromDto(it) }
    }
}

class ExerciseSource {
    lateinit var entities: List<ExerciseDto>
}