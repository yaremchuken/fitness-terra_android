package exp.yaremchuken.fitnessterra.data.database

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun instantToEntity(instant: Instant): Long = instant.toEpochMilli()
    @TypeConverter
    fun entityToInstant(millis: Long): Instant = Instant.ofEpochMilli(millis)
}