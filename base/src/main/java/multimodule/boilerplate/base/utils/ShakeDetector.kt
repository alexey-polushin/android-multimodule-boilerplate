package multimodule.boilerplate.base.utils

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener


class ShakeDetector(
    private val mShakeListener: OnShakeListener
) : SensorEventListener {
    private val mGravity = floatArrayOf(0.0f, 0.0f, 0.0f)
    private val mLinearAcceleration = floatArrayOf(0.0f, 0.0f, 0.0f)
    private var startTime: Long = 0
    private var moveCount = 0

    override fun onSensorChanged(event: SensorEvent) {
        setCurrentAcceleration(event)
        val maxLinearAcceleration = maxCurrentLinearAcceleration
        if (maxLinearAcceleration > MIN_SHAKE_ACCELERATION) {
            val now = System.currentTimeMillis()
            if (startTime == 0L) {
                startTime = now
            }
            val elapsedTime = now - startTime
            if (elapsedTime > MAX_SHAKE_DURATION) {
                resetShakeDetection()
            } else {
                moveCount++
                if (moveCount > MIN_MOVEMENTS) {
                    mShakeListener.onShake()
                    resetShakeDetection()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    private fun setCurrentAcceleration(event: SensorEvent) {
        val alpha = 0.8f

        mGravity[X] = alpha * mGravity[X] + (1 - alpha) * event.values[X]
        mGravity[Y] = alpha * mGravity[Y] + (1 - alpha) * event.values[Y]
        mGravity[Z] = alpha * mGravity[Z] + (1 - alpha) * event.values[Z]

        mLinearAcceleration[X] = event.values[X] - mGravity[X]
        mLinearAcceleration[Y] = event.values[Y] - mGravity[Y]
        mLinearAcceleration[Z] = event.values[Z] - mGravity[Z]
    }

    private val maxCurrentLinearAcceleration: Float
        get() {
            var maxLinearAcceleration = mLinearAcceleration[X]

            if (mLinearAcceleration[Y] > maxLinearAcceleration) {
                maxLinearAcceleration = mLinearAcceleration[Y]
            }

            if (mLinearAcceleration[Z] > maxLinearAcceleration) {
                maxLinearAcceleration = mLinearAcceleration[Z]
            }

            return maxLinearAcceleration
        }

    private fun resetShakeDetection() {
        startTime = 0
        moveCount = 0
    }

    interface OnShakeListener {
        fun onShake()
    }

    companion object {
        private const val MIN_SHAKE_ACCELERATION = 5
        private const val MIN_MOVEMENTS = 3
        private const val MAX_SHAKE_DURATION = 100

        private const val X = 0
        private const val Y = 1
        private const val Z = 2
    }
}