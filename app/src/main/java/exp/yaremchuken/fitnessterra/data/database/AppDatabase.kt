package exp.yaremchuken.fitnessterra.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import exp.yaremchuken.fitnessterra.data.dao.ScheduleDao
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity

@Database(
    version = 1,
    entities = [ScheduleEntity::class]
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    companion object {
        const val DATABASE_NAME = "fintessterra_db"
    }
}