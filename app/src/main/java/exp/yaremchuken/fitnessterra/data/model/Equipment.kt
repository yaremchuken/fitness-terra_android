package exp.yaremchuken.fitnessterra.data.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Equipment used during the exercise.
 */
open class EquipmentBase(

    /**
     * Type of equipment.
     */
    @JsonProperty("type")
    val type: EquipmentType,

    /**
     * Quantity of equipment for exercise, like 2 dumbbells, or 1 barbell.
     */
    @JsonProperty("quantity")
    val quantity: Long = 1
) {

}

class Equipment(
    type: EquipmentType,
    quantity: Long = 1,
    /**
     * Weight of single equipment unit, grams.
     */
    val weight: Long = 0
): EquipmentBase(type, quantity)