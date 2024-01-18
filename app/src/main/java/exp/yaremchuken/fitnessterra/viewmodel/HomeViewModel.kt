package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.ScheduleEntity
import exp.yaremchuken.fitnessterra.data.repository.ScheduleRepository
import exp.yaremchuken.fitnessterra.data.repository.WorkoutRepository
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val workoutRepository: WorkoutRepository
): ViewModel() {
    fun getTodaySchedules() = scheduleRepository.getOnDate(LocalDate.now())

    fun fromEntity(entity: ScheduleEntity) =
        scheduleRepository.fromEntity(entity, workoutRepository.getById(entity.id!!)!!)
}