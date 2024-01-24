package exp.yaremchuken.fitnessterra.data.model

import exp.yaremchuken.fitnessterra.AppSettings

/**
 * The group of muscles (or specific muscle) that the exercise is aimed at.
 */
enum class MuscleGroupType {
    NECK,
    TRAPEZOID,
    DELTOIDS,
    CHEST,
    BACK,
    LATISSIMUS_DORSI, // THE WIDEST MUSCLE OF THE BACK
    LUMBAR,
    BICEPS,
    TRICEPS,
    FOREARMS,
    ABS,
    GLUTES,
    THIGHS,
    HAMSTRINGS,
    QUADRICEPS,
    CALVES;

    companion object {
        fun i18n(type: MuscleGroupType) =
            if (AppSettings.localeIsRu()) {
                when(type) {
                    NECK -> "шея"
                    TRAPEZOID -> "трапеция"
                    DELTOIDS -> "дельтовидные мышцы"
                    CHEST -> "грудь"
                    BACK -> "спина"
                    LATISSIMUS_DORSI -> "широчайшая мышца спины"
                    LUMBAR -> "поясница"
                    BICEPS -> "бицепс"
                    TRICEPS -> "трицепс"
                    FOREARMS -> "предплечья"
                    ABS -> "пресс"
                    GLUTES -> "ягодицы"
                    THIGHS -> "бедра"
                    HAMSTRINGS -> "задние мышцы бедра"
                    QUADRICEPS -> "квадрицепс"
                    CALVES -> "икры"
                }
            } else {
                type.name.lowercase().replace("_", " ")
            }
    }
}