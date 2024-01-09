package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings
import java.util.Locale

/**
 * The level of training, needed from trainee to perform this exercise correctly.
 */
enum class LevelType {
    BEGINNER,
    ADVANCED,
    PROFESSIONAL;

    companion object {
        fun i18n(type: LevelType) =
            if (AppSettings.locale() == Locale.forLanguageTag("ru")) {
                when(type) {
                    BEGINNER -> "новичок"
                    ADVANCED -> "опытный"
                    PROFESSIONAL -> "профессионал"
                }
            } else {
                type.name.lowercase()
            }
    }
}