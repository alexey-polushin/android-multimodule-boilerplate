package multimodule.boilerplate.core.contract

class ContractInfo<Actual, Expected>(
    val name: String,
    val reason: String?,
    val actual: Actual?,
    val expected: Expected?
)