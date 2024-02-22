package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.ExerciseSetupEntity
import exp.yaremchuken.fitnessterra.data.model.Equipment
import exp.yaremchuken.fitnessterra.data.model.EquipmentType
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.ExerciseSetupRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class ExerciseSetupDetailsViewModel @Inject constructor(
    private val exerciseSetupRepository: ExerciseSetupRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {
    fun getSetup(sectionId: Long, exerciseId: Long) =
        exerciseSetupRepository.getBySectionAndExercise(sectionId, exerciseId)

    fun updateSetup(
        initial: ExerciseSetup,
        equipment: List<Equipment>,
        sets: List<Long>,
        duration: Duration,
        recovery: Duration
    ) {
        viewModelScope.launch {
            exerciseSetupRepository.insert(
                ExerciseSetup(
                    initial.sectionId,
                    initial.exercise,
                    initial.order,
                    equipment,
                    sets,
                    duration,
                    recovery
                )
            )
        }
    }

    fun fromEntity(entity: ExerciseSetupEntity) =
        ExerciseSetupRepository.fromEntity(
            entity,
            entity.sectionId,
            exerciseRepository.getByIds(listOf(entity.exerciseId)).first()
        )

    fun adjustEquipment(equipment: List<Equipment>, type: EquipmentType, weight: Long): List<Equipment> =
        equipment.map {
            if (it.type == type) {
                Equipment(type, it.quantity, it.weight + weight)
            } else {
                it
            }
        }
}