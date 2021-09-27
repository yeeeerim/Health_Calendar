package CalenderClass

class LinearRegression(xdata: ArrayList<Float>, YData: ArrayList<Float>) {
    private val Xdata: ArrayList<Float>
    private val YData: ArrayList<Float>
    private var result1: Float = 0f
    private var result2: Float = 0f
    fun predictValue(inputValue: Float): Float {
        val X1 = Xdata[0]
        val Y1 = YData[0]
        val Xmean = getXMean(Xdata)
        val Ymean = getYMean(YData)
        val lineSlope = getLineSlope(Xmean, Ymean, X1, Y1)
        val YIntercept = getYIntercept(Xmean, Ymean, lineSlope)
        return lineSlope * inputValue + YIntercept
    }

    fun getLineSlope(Xmean: Float, Ymean: Float, X1: Float, Y1: Float): Float {
        val num1 = X1 - Xmean
        val num2 = Y1 - Ymean
        val denom = (X1 - Xmean) * (X1 - Xmean)
        return num1 * num2 / denom
    }

    fun getYIntercept(Xmean: Float, Ymean: Float, lineSlope: Float): Float {
        return Ymean - lineSlope * Xmean
    }

    fun getXMean(Xdata: ArrayList<Float>): Float {
        result1 = 0.0f
        for (i in 0 until Xdata.size) {
            result1 = result1 + Xdata[i]
        }
        return result1 as Float
    }

    fun getYMean(Ydata: ArrayList<Float>): Float {
        result2 = 0.0f
        for (i in 0 until Ydata.size) {
            result2 = result2 + Ydata[i]
        }
        return result2 as Float
    }

    init {
        Xdata = xdata
        this.YData = YData
    }
}