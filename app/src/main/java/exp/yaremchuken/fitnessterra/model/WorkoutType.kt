package exp.yaremchuken.fitnessterra.model

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
            if (Locale.getDefault() == Locale.forLanguageTag("ru")) {
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
                type.name.lowercase()
            }
    }
}
