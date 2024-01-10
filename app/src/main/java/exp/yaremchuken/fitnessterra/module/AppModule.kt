package exp.yaremchuken.fitnessterra.module

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import exp.yaremchuken.fitnessterra.data.dao.ExerciseDao
import exp.yaremchuken.fitnessterra.data.dao.WorkoutDao
import exp.yaremchuken.fitnessterra.data.database.AppDatabase
import exp.yaremchuken.fitnessterra.data.datasource.YamlDatasource
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
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
    fun workoutRepository(datasource: YamlDatasource) = WorkoutRepository(WorkoutDao(datasource))

    @Provides
    @Singleton
    fun appDatabase(app: Application) =
        Room.databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun scheduleRepository(appDatabase: AppDatabase) = ScheduleRepository(appDatabase.scheduleDao())
}