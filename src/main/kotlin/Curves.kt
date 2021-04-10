object curves {
    var scale: Any = 50.0
        set(value) {
            field = when (value) {
                is Int    -> value.toDouble()
                is Double -> value
                else      -> throw InstantiationError("Can't init scale with ${value.javaClass}")
            }

            PlotWindow.scale = scale as Double
        }

    fun curve(initCurve: Curve.() -> Unit) {
        val curve = Curve()
        curve.initCurve()

        if (curve.range == null || curve.x == null || curve.y == null)
            throw InstantiationError("range, x or y aren't initialized!")

        PlotWindow.addCurve(curve)
    }

    operator fun invoke(initCurves: curves.() -> Unit) {
        initCurves()
        PlotWindow.pack()
        PlotWindow.isVisible = true
    }
}