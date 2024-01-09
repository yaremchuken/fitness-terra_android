package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings
import java.util.Locale

/**
 * Main activity type of the workout.
 */
enum class WorkoutType {
    CARDIO,
    STRENGTH,
    FLEXIBILITY,
    BALANCE,
    COORDINATION,
    CYCLING,
    WALKING,
    RUNNING,
    MARTIAL;

    companion object {
        fun i18n(type: WorkoutType) =
            if (AppSettings.locale() == Locale.forLanguageTag("ru")) {
                when(type) {
                    CARDIO -> "кардио"
                    STRENGTH -> "сила"
                    FLEXIBILITY -> "гибкость"
                    BALANCE -> "баланс"
                    COORDINATION -> "координация"
                    CYCLING -> "велосипед"
                    WALKING -> "ходьба"
                    RUNNING -> "бег"
                    MARTIAL -> "бой"
                }
            } else {
                type.name.lowercase().replace("_", " ")
            }
    }
}
