package multimodule.boilerplate.base.format

import android.content.Context
import multimodule.boilerplate.base.R
import multimodule.boilerplate.core.format.DisplayFormat
import multimodule.boilerplate.core.format.Formatter

class ContextDisplayFormat(
    private val _context: Context,
) : DisplayFormat {
    /**
     * 00:51 секунда, 11:28 секунд, 00:44 минуты
     */
    override fun secondsToTimeFormatter(): Formatter<Long> =
        TimeSecondsFormatter(
            _context.resources,
            R.plurals.common__seconds,
            R.plurals.common__minutes
        )

    /**
     * 00:51 секунду, 11:28 секунд, 00:44 минуты
     */
    override fun secondsToTimeAccusativeFormatter(): Formatter<Long> =
        TimeSecondsFormatter(
            _context.resources,
            R.plurals.common__seconds_accusative,
            R.plurals.common__minutes_aсcusative
        )

    /**
     * 00:59 сек, 11:28 сек, 00:41 мин
     */
    override fun secondsToTimeShortFormatter(): Formatter<Long> =
        TimeSecondsShortFormatter(
            _context.resources,
            R.string.common__seconds_short,
            R.string.common__minutes_short
        )
}