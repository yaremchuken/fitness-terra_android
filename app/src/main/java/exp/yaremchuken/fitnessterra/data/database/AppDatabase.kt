package exp.yaremchuken.fitnessterra.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import exp.yaremchuken.fitnessterra.data.dao.ExerciseSetupDao
import exp.yaremchuken.fitnessterra.data.dao.HistoryDao
import exp.yaremchuken.fitnessterra.data.dao.ScheduleDao
import exp.yaremchuken.fitnessterra.data.dao.WorkoutDao
import exp.yaremchuken.fitnessterra.data.dao.WorkoutSectionDao
import exp.yaremchuken.fitnessterra.data.dao.WorkoutSequenceDao
import exp.yaremchuken.fitnessterra.data.entity.ExerciseSetupEntity
import exp.yaremchuken.fitnessterra.data.entity.HistoryEntity
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSectionEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceEntity
import exp.yaremchuken.fitnessterra.data.entity.WorkoutSequenceLinkEntity

@Database(
    version = 24,
    entities = [
        ScheduleEntity::class,
        HistoryEntity::class,
        ExerciseSetupEntity::class,
        WorkoutSectionEntity::class,
        WorkoutEntity::class,
        WorkoutSequenceEntity::class,
        WorkoutSequenceLinkEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun scheduleDao(): ScheduleDao

    abstract fun historyDao(): HistoryDao

    abstract fun exerciseSetupDao(): ExerciseSetupDao

    abstract fun workoutSectionDao(): WorkoutSectionDao

    abstract fun workoutDao(): WorkoutDao

    abstract fun workoutSequenceDao(): WorkoutSequenceDao

    companion object {
        const val DATABASE_NAME = "fitness-terra_db"
    }
}