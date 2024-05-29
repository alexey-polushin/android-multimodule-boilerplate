package multimodule.boilerplate.core.format

interface DisplayFormat {
    fun secondsToTimeFormatter(): Formatter<Long>
    fun secondsToTimeAccusativeFormatter(): Formatter<Long>
    fun secondsToTimeShortFormatter(): Formatter<Long>
}