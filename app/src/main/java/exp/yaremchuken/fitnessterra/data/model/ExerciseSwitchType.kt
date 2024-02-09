package exp.yaremchuken.fitnessterra.data.model

/**
 * Describes repeats for each side of body.
 * Same exercises should be performed for left or right "body" side separately,
 * for example: "Alternate bending of the arms" should be performed by left arm and then by right arm by turns in same set,
 * another example is "One-handed extensions with a dumbbell" where one set should be performed by left arm
 * and then the same set should be performed by right arm
 */
enum class ExerciseSwitchType {
    /**
     * Exercise performed once, without any side change repeats.
     */
    NO_SIDE_SWITCH,

    /**
     * After every side repeat where have to be the other side repeat.
     */
    SIDE_SWITCH_ON_REPEAT,

    /**
     * After set performed with one side, the same set should be performed by other side.
     */
    SIDE_SWITCH_ON_SET
}