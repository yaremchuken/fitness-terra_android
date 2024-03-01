package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings

/**
 * Type of equipment used in exercises.
 */
enum class EquipmentType {
    BARBELL,
    DUMBBELLS,
    BENCH,
    CARPET,
    PLATFORM;

    companion object {
        fun i18n(type: EquipmentType) =
            if (AppSettings.localeIsRu()) {
                when(type) {
                    BARBELL -> "штанга"
                    DUMBBELLS -> "гантели"
                    BENCH -> "Скамья"
                    CARPET -> "коврик"
                    PLATFORM -> "платформа"
                }
            } else {
                type.name.lowercase().replace("_", " ")
            }
    }
}
