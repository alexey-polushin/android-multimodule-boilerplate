package multimodule.boilerplate.core.contract

import common.library.core.contract.Contract

class ConditionContract<Value>(
    private val _condition: () -> Result<Value>,
    private val _error: (Value) -> ContractException
) : Contract {
    private class CheckResult(
        val check: Boolean,
        val error: Throwable?
    )

    private val _resultLock = Any()

    @Volatile
    private var _result: CheckResult? = null

    private fun _checkInternal(): CheckResult {
        synchronized(_resultLock) {
            var result = _result
            if (result === null) {
                val conditionResult = _condition()
                val check = conditionResult.condition
                val error = if (!check) {
                    _error(conditionResult.value)
                } else {
                    null
                }

                result = CheckResult(check, error)
                _result = result
            }

            return result
        }
    }

    override fun assert() {
        val result = _checkInternal()
        if (!result.check) {
            throw result.error!!
        }
    }

    override fun check(): Boolean = _checkInternal().check

    data class Result<Value>(
        val condition: Boolean,
        val value: Value
    )
}