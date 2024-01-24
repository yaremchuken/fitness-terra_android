package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings

/**
 * The level of training, needed from trainee to perform this exercise correctly.
 */
enum class LevelType {
    BEGINNER,
    ADVANCED,
    PROFESSIONAL;

    companion object {
        fun i18n(type: LevelType) =
            if (AppSettings.localeIsRu()) {
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