package multimodule.boilerplate.core.contract

import common.library.core.contract.Contract
import kotlinx.collections.immutable.ImmutableCollection

class CompositeContract(private val _contracts: ImmutableCollection<Contract>) : Contract {
    override fun assert() = _contracts.forEach { it.assert() }

    override fun check(): Boolean = _contracts.all { it.check() }
}