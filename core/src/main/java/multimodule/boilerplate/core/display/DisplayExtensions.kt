package multimodule.boilerplate.core.display

import android.app.Activity
import androidx.window.layout.WindowMetricsCalculator

fun Activity.getDisplayWidthPixels(): Int =
    WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this).bounds.width()

fun Activity.getDisplayHeightPixels(): Int =
    WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this).bounds.height()