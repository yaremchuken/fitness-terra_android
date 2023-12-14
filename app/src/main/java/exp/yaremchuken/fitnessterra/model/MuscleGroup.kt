package exp.yaremchuken.fitnessterra.model

import java.util.Locale

/**
 * The main group of muscles that the exercise is aimed at.
 */
enum class MuscleGroup {
    NECK,
    SHOULDERS,
    CHEST,
    BACK,
    BICEPS,
    TRICEPS,
    FOREARMS,
    ABS,
    GLUTES,
    THIGHS,
    CALVES;

    companion object {
        fun i18n(type: MuscleGroup) =
            if (Locale.getDefault() == Locale.forLanguageTag("ru")) {
                when(type) {
                    NECK -> "шея"
                    SHOULDERS -> "плечи"
                    CHEST -> "грудь"
                    BACK -> "спина"
                    BICEPS -> "бицепс"
                    TRICEPS -> "трицепс"
                    FOREARMS -> "руки"
                    ABS -> "пресс"
                    GLUTES -> "ягодицы"
                    THIGHS -> "бедра"
                    CALVES -> "Икры"
                }
            } else {
                type.name.lowercase()
            }
    }
}