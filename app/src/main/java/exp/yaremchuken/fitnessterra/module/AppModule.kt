package exp.yaremchuken.fitnessterra.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import exp.yaremchuken.fitnessterra.data.dao.ExerciseDao
import exp.yaremchuken.fitnessterra.data.database.AppDatabase
import exp.yaremchuken.fitnessterra.data.datasource.YamlDatasource
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.ExerciseSetupRepository
import exp.yaremchuken.fitnessterra.data.repository.HistoryRepository
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutSectionRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutSequenceRepository
import exp.yaremchuken.fitnessterra.service.TextToSpeechHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun yamlDatasource(app: Application) = YamlDatasource(app)

    @Provides
    @Singleton
    fun exerciseRepository(datasource: YamlDatasource) = ExerciseRepository(ExerciseDao(datasource))

    @Provides
    @Singleton
    fun appDatabase(app: Application) =
        Room.databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .createFromAsset("datasource/fitness-terra_db.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun exerciseSetupRepository(appDatabase: AppDatabase) = ExerciseSetupRepository(appDatabase.exerciseSetupDao())

    @Provides
    @Singleton
    fun workoutSectionRepository(appDatabase: AppDatabase) = WorkoutSectionRepository(appDatabase.workoutSectionDao())

    @Provides
    @Singleton
    fun workoutRepository(appDatabase: AppDatabase) = WorkoutRepository(appDatabase.workoutDao())

    @Provides
    @Singleton
    fun scheduleRepository(appDatabase: AppDatabase) = ScheduleRepository(appDatabase.scheduleDao())

    @Provides
    @Singleton
    fun historyRepository(appDatabase: AppDatabase) = HistoryRepository(appDatabase.historyDao())

    @Provides
    @Singleton
    fun workoutSequenceRepository(appDatabase: AppDatabase) = WorkoutSequenceRepository(appDatabase.workoutSequenceDao())

    @Provides
    @Singleton
    fun textToSpeechHelper(app: Application) = TextToSpeechHelper(app)
}