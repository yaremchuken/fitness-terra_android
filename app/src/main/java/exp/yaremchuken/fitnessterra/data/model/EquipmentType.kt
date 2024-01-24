package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings

/**
 * Type of equipment used in exercises.
 */
enum class EquipmentType {
    BARBELL,
    DUMBBELLS,
    CARPET;

    companion object {
        fun i18n(type: EquipmentType) =
            if (AppSettings.localeIsRu()) {
                when(type) {
                    BARBELL -> "штанга"
                    DUMBBELLS -> "гантели"
                    CARPET -> "коврик"
                }
            } else {
                type.name.lowercase()
            }
    }
}
