package multimodule.boilerplate.core.contract

import multimodule.boilerplate.core.contract.ContractsBuilder.*
import common.library.core.contract.Contract
import common.library.core.contract.Contracts
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.util.regex.Pattern
import javax.annotation.CheckReturnValue

private typealias ExpectedProvider = () -> Any?
private typealias Rule = (Any?, Any?) -> Boolean
private typealias ErrorProvider = (String, String?, Any?, Any?) -> Throwable

data class ImmutableContractsBuilder private constructor(
    private val _contracts: PersistentList<BuildContract> = persistentListOf()
) : Start,
    Value,
    Violation<Any?, Any?>,
    Because<Any?, Any?>,
    BuildOrNext,
    ContractsBuilder {
    companion object {
        private var _empty = ImmutableContractsBuilder()

        val start: Start = _empty

        val empty: ContractsBuilder = _empty

        private fun String?._prependBecause() = if (this == null) "" else " because $this"
    }

    private data class BuildContract
    private constructor(
        val valueName: String = defaultValueName,
        val actual: Any? = null,
        val reason: String? = null,
        val expected: ExpectedProvider? = null,
        val rule: Rule? = null,
        val error: ErrorProvider? = null
    ) : Contract {
        companion object {
            val empty = BuildContract()

            const val defaultValueName = "Value"

            @CheckReturnValue
            fun of(
                actual: Any?,
                name: String?
            ) = empty.copy(actual = actual, valueName = name ?: defaultValueName)
        }

        private class Result(
            val check: Boolean,
            val error: Throwable?
        )

        private val _resultLock = Any()

        @Volatile
        private var _result: Result? = null

        private fun _checkInternal(): Result {
            synchronized(_resultLock) {
                var result = _result
                if (result === null) {
                    if (expected == null || rule == null || error == null) {
                        unreachable()
                    }

                    val actualInternal = actual
                    val expectedInternal = expected.invoke()

                    val check = rule.invoke(actualInternal, expectedInternal)
                    val error = if (!check) {
                        error.invoke(
                            valueName,
                            reason,
                            actualInternal,
                            expectedInternal
                        )
                    } else {
                        null
                    }

                    result = Result(check, error)
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
    }

    //region Value

    @Suppress("UNCHECKED_CAST")
    override fun <Actual> value(
        actual: Actual,
        name: String?
    ): ComparisonConstraints<Actual> {
        val builder = copy(
            _contracts = _contracts.add(BuildContract.of(actual, name))
        )

        return object : ComparisonConstraintsProxy<Actual> {
            override val builder: ImmutableContractsBuilder = builder
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <Actual : Comparable<Actual>> value(
        actual: Actual?,
        name: String?
    ): ComparableConstraints<Actual> {
        val builder = copy(
            _contracts = _contracts.add(BuildContract.of(actual, name))
        )

        return object : ComparableConstraintsProxy<Actual> {
            override val builder: ImmutableContractsBuilder = builder
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun value(
        actual: String?,
        name: String?
    ): StringConstraints {
        val builder = copy(
            _contracts = _contracts.add(BuildContract.of(actual, name))
        )

        return object : StringConstraintsProxy {
            override val builder: ImmutableContractsBuilder = builder
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun value(
        actual: Int?,
        name: String?
    ): IntConstraints {
        val builder = copy(
            _contracts = _contracts.add(BuildContract.of(actual, name))
        )

        return object : IntConstraintProxy {
            override val builder: ImmutableContractsBuilder = builder
        }
    }

    override fun value(
        actual: Boolean?,
        name: String?
    ): BooleanConstraints {
        val builder = copy(
            _contracts = _contracts.add(BuildContract.of(actual, name))
        )

        return object : BooleanConstraintsProxy {
            override val builder: ImmutableContractsBuilder = builder
        }
    }

    override fun <Actual : Collection<ActualItem>, ActualItem> value(
        actual: Actual?,
        name: String?
    ): CollectionConstraints<Actual, ActualItem> {
        val builder = copy(
            _contracts = _contracts.add(BuildContract.of(actual, name))
        )

        return object : CollectionConstraintsProxy<Actual, ActualItem> {
            override val builder: ImmutableContractsBuilder = builder
        }
    }

    //endregion Value

    private interface Proxy {
        val builder: ImmutableContractsBuilder
    }

    private interface RuleConstraintsProxy<Actual>
        : RuleConstraints<Actual>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun mustBe(rule: (Actual) -> Boolean): Because<Actual, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ -> rule(actual as Actual) },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must satisfy the rule${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBe(rule: (Actual) -> Boolean): Because<Actual, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ -> !rule(actual as Actual) },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must not satisfy the rule${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Unit>
    }

    private interface NullabilityConstraintsProxy<Actual>
        : NullabilityConstraints<Actual>,
        RuleConstraintsProxy<Actual>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun mustBeNull(): Because<Actual, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ -> actual === null },
                    error = { valueName, reason, _, _ ->
                        ContractException("$valueName must be null${reason._prependBecause()}.")
                    }
                )
            } as Because<Actual, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBeNull(): Because<Actual, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ -> actual !== null },
                    error = { valueName, reason, _, _ ->
                        ContractException("$valueName must be not null${reason._prependBecause()}.")
                    }
                )
            } as Because<Actual, Unit>
    }

    private interface EqualityConstraintsProxy<Actual> :
        EqualityConstraints<Actual>,
        NullabilityConstraintsProxy<Actual>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun mustBeEqual(expected: () -> Actual): Because<Actual, Actual> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected -> actual == expected },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be equal to `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Actual>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBeEqual(expected: () -> Actual): Because<Actual, Actual> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected -> actual != expected },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must not be equal to `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Actual>
    }

    private interface ComparisonConstraintsProxy<Actual>
        : ComparisonConstraints<Actual>,
        EqualityConstraintsProxy<Actual>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun <Expected : Comparable<Actual>> mustBeExact(expected: () -> Expected): Because<Actual, Expected> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected ->
                        (expected as Comparable<Any?>).compareTo(actual) == 0
                    },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be equal to `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Expected>

        @Suppress("UNCHECKED_CAST")
        override fun <Expected : Comparable<Actual>> mustNotBeExact(expected: () -> Expected): Because<Actual, Expected> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected ->
                        (expected as Comparable<Any?>).compareTo(actual) != 0
                    },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must not be equal to `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Expected>

        @Suppress("UNCHECKED_CAST")
        override fun <Expected : Comparable<Actual>> mustBeGreater(expected: () -> Expected): Because<Actual, Expected> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected ->
                        (expected as Comparable<Any?>) < actual
                    },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be greater than `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Expected>

        @Suppress("UNCHECKED_CAST")
        override fun <Expected : Comparable<Actual>> mustBeGreaterOrEqual(expected: () -> Expected): Because<Actual, Expected> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected -> (expected as Comparable<Any?>) <= actual },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be greater than or equal to `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Expected>

        @Suppress("UNCHECKED_CAST")
        override fun <Expected : Comparable<Actual>> mustBeLess(expected: () -> Expected): Because<Actual, Expected> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected -> (expected as Comparable<Any?>) > actual },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be less than `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Expected>

        @Suppress("UNCHECKED_CAST")
        override fun <Expected : Comparable<Actual>> mustBeLessOrEqual(expected: () -> Expected): Because<Actual, Expected> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected -> (expected as Comparable<Any?>) >= actual },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be less than or equal to `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, Expected>
    }

    private interface ComparableConstraintsProxy<Actual : Comparable<Actual>> :
        ComparableConstraints<Actual>,
        ComparisonConstraintsProxy<Actual>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun mustBeInRange(expected: () -> ClosedRange<Actual>): Because<Actual, ClosedRange<Actual>> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected ->
                        (actual as Actual) in (expected as ClosedRange<Actual>)
                    },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be in range `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, ClosedRange<Actual>>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBeInRange(expected: () -> ClosedRange<Actual>): Because<Actual, ClosedRange<Actual>> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected ->
                        (actual as Actual) !in (expected as ClosedRange<Actual>)
                    },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must not be in range `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Actual, ClosedRange<Actual>>
    }

    private interface StringConstraintsProxy
        : StringConstraints,
        ComparableConstraintsProxy<String>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun mustMatch(expected: () -> Pattern): Because<String, Pattern> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected ->
                        actual is CharSequence && (expected as Pattern).matcher(actual).matches()
                    },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must much pattern `${(expected as Pattern).pattern()}`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<String, Pattern>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotMatch(expected: () -> Pattern): Because<String, Pattern> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected ->
                        actual !is CharSequence || !(expected as Pattern).matcher(actual).matches()
                    },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must not much pattern `${(expected as Pattern).pattern()}`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<String, Pattern>
    }

    private interface IntConstraintProxy
        : IntConstraints,
        ComparableConstraintsProxy<Int>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun mustBePositive(): Because<Int, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ ->
                        (actual as Int) > 0
                    },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must be positive${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Int, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBePositive(): Because<Int, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ ->
                        (actual as Int) <= 0
                    },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must not be positive${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Int, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustBeNegative(): Because<Int, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ ->
                        (actual as Int) < 0
                    },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must be negative${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Int, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBeNegative(): Because<Int, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ ->
                        (actual as Int) >= 0
                    },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must not be negative${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Int, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustBeZero(): Because<Int, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ ->
                        (actual as Int) == 0
                    },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must be zero${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Int, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBeZero(): Because<Int, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ ->
                        (actual as Int) != 0
                    },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must not be zero${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Int, Unit>
    }

    private interface CollectionConstraintsProxy<Actual : Collection<ActualItem>, ActualItem>
        : CollectionConstraints<Actual, ActualItem>,
        NullabilityConstraintsProxy<Actual>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun mustNotBeEmpty(): Because<Actual, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ -> (actual as Collection<*>).isNotEmpty() },
                    error = { valueName, reason, _, _ ->
                        ContractException("$valueName must be not an empty collection${reason._prependBecause()}.")
                    }
                )
            } as Because<Actual, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustBeEmpty(): Because<Actual, Unit> =
            builder.mutateLastContract {
                copy(
                    expected = { Unit },
                    rule = { actual, _ -> (actual as Collection<*>).isEmpty() },
                    error = { valueName, reason, actual, _ ->
                        ContractException("$valueName must be an empty collection${reason._prependBecause()}. But actual size is `${(actual as Collection<*>).size}`.")
                    }
                )
            } as Because<Actual, Unit>

        @Suppress("UNCHECKED_CAST")
        override fun mustHaveSize(expected: () -> Int): Because<Actual, Int> =
            builder.mutateLastContract {
                copy(
                    expected = expected,
                    rule = { actual, expected -> (actual as Collection<*>).size == expected as Int },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must have size `$expected`${reason._prependBecause()}. But actual size is `${(actual as Collection<*>).size}`.")
                    }
                )
            } as Because<Actual, Int>
    }

    private interface BooleanConstraintsProxy
        : BooleanConstraints,
        ComparableConstraintsProxy<Boolean>,
        Proxy {
        @Suppress("UNCHECKED_CAST")
        override fun mustBeTrue(): Because<Boolean, Boolean> =
            builder.mutateLastContract {
                copy(
                    expected = { true },
                    rule = { actual, expected -> actual as Boolean? == expected },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Boolean, Boolean>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBeTrue(): Because<Boolean, Boolean> =
            builder.mutateLastContract {
                copy(
                    expected = { true },
                    rule = { actual, expected -> actual as Boolean? != expected },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Boolean, Boolean>

        @Suppress("UNCHECKED_CAST")
        override fun mustBeFalse(): Because<Boolean, Boolean> =
            builder.mutateLastContract {
                copy(
                    expected = { false },
                    rule = { actual, expected -> actual as Boolean? == expected },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Boolean, Boolean>

        @Suppress("UNCHECKED_CAST")
        override fun mustNotBeFalse(): Because<Boolean, Boolean> =
            builder.mutateLastContract {
                copy(
                    expected = { false },
                    rule = { actual, expected -> actual as Boolean? != expected },
                    error = { valueName, reason, actual, expected ->
                        ContractException("$valueName must be `$expected`${reason._prependBecause()}, but actual value: `$actual`.")
                    }
                )
            } as Because<Boolean, Boolean>
    }

    override fun because(reason: String): Violation<Any?, Any?> = mutateLastContract {
        copy(reason = reason)
    }

    private inline fun mutateLastContract(action: BuildContract.() -> BuildContract): ImmutableContractsBuilder {
        val lastIndex = _contracts.lastIndex
        val lastContract = _contracts[lastIndex]

        return copy(
            _contracts = _contracts.set(lastIndex, action(lastContract))
        )
    }

    override fun orError(description: (ContractInfo<Any?, Any?>) -> String): BuildOrNext =
        mutateLastContract {
            copy(error = { valueName, reason, actual, expected ->
                ContractException(description(ContractInfo(valueName, reason, actual, expected)))
            })
        }

    override fun orCustomError(description: (ContractInfo<Any?, Any?>) -> Throwable): BuildOrNext =
        mutateLastContract {
            copy(error = { valueName, reason, actual, expected ->
                description(ContractInfo(valueName, reason, actual, expected))
            })
        }

    override fun build() = Contracts.compose(_contracts)
}