package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings

/**
 * Type of equipment used in exercises.
 */
enum class EquipmentType {
    BARBELL,
    DUMBBELLS,
    WORKOUT_BENCH,
    CARPET,
    STEP_PLATFORM;

    companion object {
        fun i18n(type: EquipmentType) =
            if (AppSettings.localeIsRu()) {
                when(type) {
                    BARBELL -> "штанга"
                    DUMBBELLS -> "гантели"
                    WORKOUT_BENCH -> "Силовая скамья"
                    CARPET -> "коврик"
                    STEP_PLATFORM -> "степ платформа"
                }
            } else {
                type.name.lowercase().replace("_", " ")
            }
    }
}
