package com.rulhouse.easyCameraX.camera

import android.graphics.Point
import android.view.MotionEvent
import androidx.camera.core.Camera

class CameraZoomGestureEvent {
    private var mPtrCount: Int = 0
    private lateinit var point1: Point
    private lateinit var point2: Point

    fun gestureEvent (event: CameraEvent) {
        when(event) {
            is CameraEvent.ZoomGesture -> {
                when (event.event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        mPtrCount++
                        if (mPtrCount == 2)
                            savePointInfo(event.event)
                    }
                    MotionEvent.ACTION_POINTER_UP -> {
                        mPtrCount--
                        if (mPtrCount == 2)
                            savePointInfo(event.event)
                    }
                    MotionEvent.ACTION_DOWN -> {
                        mPtrCount++
                    }
                    MotionEvent.ACTION_UP -> {
                        mPtrCount--
                    }
                }
            }
            else -> {}
        }
    }

    fun zoomEvent (event: CameraEvent, camera: Camera): Float {
        when(event) {
            is CameraEvent.ZoomGesture -> {
                when (event.event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_MOVE -> {
                        val value = camera.cameraInfo.zoomState.value
                        return if (mPtrCount == 2) {
                            getZoomRatio(
                                event.event,
                                value!!.zoomRatio,
                                value.minZoomRatio,
                                value.maxZoomRatio
                            )
                        } else {
                            value!!.zoomRatio
                        }
                    }
                }
            }
            else -> {}
        }
        return 1.0f
    }

    private fun savePointInfo(event: MotionEvent) {
        point1 = Point(event.getX(0).toInt(), event.getY(0).toInt())
        point2 = Point(event.getX(1).toInt(), event.getY(1).toInt())
    }

    private fun getZoomRatio(event: MotionEvent, zoomRatio: Float, minZoomRatio: Float, maxZoomRatio: Float): Float {
        val newPoint1 = Point(event.getX(0).toInt(), event.getY(0).toInt())
        val newPoint2 = Point(event.getX(1).toInt(), event.getY(1).toInt())

        val oriDistance = CameraCalculator.getDistance(point1, point2)
        val newDistance = CameraCalculator.getDistance(newPoint1, newPoint2)

        savePointInfo(event)

        var newRatio: Float = ((newDistance / oriDistance) * zoomRatio).toFloat()
        if (newRatio < minZoomRatio) newRatio = minZoomRatio
        else if (newRatio > maxZoomRatio) newRatio = maxZoomRatio

        return newRatio
    }
}