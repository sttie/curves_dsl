class Curve {
    val t = IntegerWrapper()
    var range: IntRange? = null

    // Я честно хотел сделать что-то похожее на перегрузку оператора "=" для Int и IntegerWrapper,
    // но в Котлине нельзя перегружать даже сеттеры... (https://youtrack.jetbrains.com/issue/KT-4075)
    var x: Any? = null
        set(anyValue) {
            field = when (anyValue) {
                is Int -> anyValue.toIntegerWrapper()
                is Double -> anyValue.toIntegerWrapper()
                is IntegerWrapper -> anyValue
                else -> throw InstantiationError("Can't init x with ${anyValue?.javaClass}")
            }
        }

    var y: Any? = null
        set(anyValue) {
            field = when (anyValue) {
                is Int -> anyValue.toIntegerWrapper()
                is Double -> anyValue.toIntegerWrapper()
                is IntegerWrapper -> anyValue
                else -> throw InstantiationError("Can't init y with ${anyValue?.javaClass}")
            }
        }

    fun getRangeList(): List<Int> = range!!.toList()

    fun evaluate(tValue: Double): Pair<Double, Double> {
        return Pair((x as IntegerWrapper).evaluate(tValue), (y as IntegerWrapper).evaluate(tValue))
    }
}