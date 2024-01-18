package exp.yaremchuken.fitnessterra.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import exp.yaremchuken.fitnessterra.data.dao.HistoryDao
import exp.yaremchuken.fitnessterra.data.dao.ScheduleDao
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity

@Database(
    version = 7,
    entities = [
        ScheduleEntity::class,
        HistoryEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    abstract fun historyDao(): HistoryDao

    companion object {
        const val DATABASE_NAME = "fintessterra_db"
    }
}