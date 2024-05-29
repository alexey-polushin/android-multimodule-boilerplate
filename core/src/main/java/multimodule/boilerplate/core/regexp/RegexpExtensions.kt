package multimodule.boilerplate.core.regexp

fun String.isPasswordValid(): Boolean {
    val passwordRegex = """^(?=.*[A-Za-z])(?=.*\d)(\S){8,32}${'$'}""".trimIndent()
    return passwordRegex.toRegex().matches(this)
}

fun String.isSecretAnswerValid(): Boolean {
    val answerRegex = """^[A-Za-zА-Яа-я]{2,20}${'$'}""".trimIndent()
    return answerRegex.toRegex().matches(this)
}

