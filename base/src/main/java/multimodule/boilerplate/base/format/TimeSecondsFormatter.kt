package multimodule.boilerplate.base.format

import android.content.res.Resources
import androidx.annotation.PluralsRes
import multimodule.boilerplate.core.format.Formatter
import javax.annotation.CheckReturnValue

class TimeSecondsFormatter(
    private val resources: Resources,
    @PluralsRes private val secondsRes: Int,
    @PluralsRes private val minutesRes: Int,
) : Formatter<Long> {
    @CheckReturnValue
    override fun format(data: Long): String {
        return when {
            data < 3600 -> String.format(
                "%02d:%02d ${resources.getQuantityString(secondsRes, data.toInt())}",
                data / 60,
                data % 60
            )

            else -> String.format(
                "%02d:%02d ${resources.getQuantityString(minutesRes, data.toInt())}",
                data / 3600,
                (data % 3600) / 60
            )
        }
    }
}