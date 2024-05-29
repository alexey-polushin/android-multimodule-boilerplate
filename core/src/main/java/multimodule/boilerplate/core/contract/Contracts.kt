package common.library.core.contract

import multimodule.boilerplate.core.contract.CompositeContract
import multimodule.boilerplate.core.contract.ConditionContract
import multimodule.boilerplate.core.contract.ContractException
import multimodule.boilerplate.core.contract.UnreachableException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.annotation.CheckReturnValue

object Contracts {
    @CheckReturnValue
    fun <Value> condition(
        condition: () -> ConditionContract.Result<Value>,
        description: (Value) -> String
    ) = ConditionContract(condition) {
        ContractException(description(it))
    }

    @CheckReturnValue
    fun unreachable() = ConditionContract({ ConditionContract.Result(false, Unit) }) {
        UnreachableException()
    }

    @CheckReturnValue
    fun compose(contracts: ImmutableList<Contract>) = CompositeContract(contracts)

    @CheckReturnValue
    fun compose(vararg contracts: Contract) = compose(persistentListOf(*contracts))
}