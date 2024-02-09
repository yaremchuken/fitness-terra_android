package exp.yaremchuken.fitnessterra.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import exp.yaremchuken.fitnessterra.data.entity.ExerciseSetupEntity
import exp.yaremchuken.fitnessterra.data.model.ExerciseSetup
import exp.yaremchuken.fitnessterra.data.repository.ExerciseRepository
import exp.yaremchuken.fitnessterra.data.repository.ExerciseSetupRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ExerciseSetupDetailsViewModel @Inject constructor(
    private val exerciseSetupRepository: ExerciseSetupRepository,
    private val exerciseRepository: ExerciseRepository
): ViewModel() {
    fun getSetup(sectionId: Long, exerciseId: Long) =
        exerciseSetupRepository.getBySectionAndExercise(sectionId, exerciseId)

    fun updateSetup(initial: ExerciseSetup, weight: Long, sets: List<Long>, duration: Duration, recovery: Duration) {
        viewModelScope.launch {
            exerciseSetupRepository.insert(
                ExerciseSetup(
                    initial.sectionId,
                    initial.exercise,
                    initial.order,
                    weight,
                    sets,
                    duration,
                    recovery
                )
            )
        }
    }

    fun fromEntity(entity: ExerciseSetupEntity) =
        ExerciseSetup(
            entity.sectionId,
            exerciseRepository.getByIds(listOf(entity.exerciseId)).first(),
            entity.order,
            entity.weight,
            Gson().fromJson(entity.sets, Array<Long>::class.java).toList(),
            entity.duration.seconds,
            entity.recovery.seconds
        )
}