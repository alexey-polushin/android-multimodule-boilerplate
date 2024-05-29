package multimodule.boilerplate.core.value.core

import multimodule.boilerplate.core.debug.DebugRepresentation
import multimodule.boilerplate.core.debug.formatDebugSingle
import javax.annotation.CheckReturnValue

abstract class StatefulAbstractValue<
    State : Any,
    Self : StatefulAbstractValue<State, Self>>(
    protected val internalState: State
) : AbstractValue<Self>() {
    @CheckReturnValue
    protected abstract fun create(state: State): Self

    @CheckReturnValue
    protected inline fun mutate(action: State.() -> State) = create(action(internalState))

    @CheckReturnValue
    override fun internalEquals(other: Self): Boolean {
        return internalState == other.internalState
    }

    @CheckReturnValue
    override fun internalHashCode(): Int {
        return internalState.hashCode()
    }

    @CheckReturnValue
    override fun internalToString(): String {
        return formatDebugSingle(DebugRepresentation.Compact, internalState.toString())
    }

    @CheckReturnValue
    fun exposeState(): State = internalState
}