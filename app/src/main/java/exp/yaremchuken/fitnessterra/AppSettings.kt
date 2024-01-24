package exp.yaremchuken.fitnessterra

import java.util.Locale

/**
 * Temporary solution to keep some app configuration.
 */
object AppSettings {

    /**
     * Right now app supports only two locales EN and RU.
     */
    fun locale(): Locale =
        if (localeIsRu()) Locale.getDefault()
        else Locale.ENGLISH

    fun localeIsRu() = Locale.getDefault().language.lowercase() == "ru"
}