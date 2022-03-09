package com.rulhouse.easyCameraX.camera

import android.graphics.Point
import kotlin.math.pow
import kotlin.math.sqrt

object CameraCalculator {
    fun getDistance(point1: Point, point2: Point): Double {
        return sqrt((point1.x - point2.x).toDouble().pow(2.0) +
                (point1.y - point2.y).toDouble().pow(2.0)
        )
    }
}