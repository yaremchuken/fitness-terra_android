package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings
import java.util.Locale

/**
 * Type of equipment used in exercises.
 */
enum class EquipmentType {
    BARBELL,
    DUMBBELLS,
    CARPET;

    companion object {
        fun i18n(type: EquipmentType) =
            if (AppSettings.locale() == Locale.forLanguageTag("ru")) {
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
