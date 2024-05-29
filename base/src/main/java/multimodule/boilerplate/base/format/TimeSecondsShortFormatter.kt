package multimodule.boilerplate.base.format

import android.content.res.Resources
import androidx.annotation.StringRes
import multimodule.boilerplate.core.format.Formatter
import javax.annotation.CheckReturnValue

class TimeSecondsShortFormatter(
    private val resources: Resources,
    @StringRes private val secondsRes: Int,
    @StringRes private val minutesRes: Int,
) : Formatter<Long> {
    @CheckReturnValue
    override fun format(data: Long): String {
        return when {
            data < 3600 -> String.format(
                "%02d:%02d ${resources.getString(secondsRes)}",
                data / 60,
                data % 60
            )

            else -> String.format(
                "%02d:%02d ${resources.getString(minutesRes)}",
                data / 3600,
                (data % 3600) / 60
            )
        }
    }
}