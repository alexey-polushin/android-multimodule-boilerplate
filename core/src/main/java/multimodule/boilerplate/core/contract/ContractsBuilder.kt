package multimodule.boilerplate.core.contract

import common.library.core.contract.Contract
import java.util.regex.Pattern
import javax.annotation.CheckReturnValue
import kotlin.reflect.KClass

interface ContractsBuilder {
    interface Start : Value

    interface Value {
        @CheckReturnValue
        fun <Actual> value(
            actual: Actual,
            name: String? = null
        ): ComparisonConstraints<Actual>

        @CheckReturnValue
        fun <Actual : Comparable<Actual>> value(
            actual: Actual?,
            name: String? = null
        ): ComparableConstraints<Actual>

        @CheckReturnValue
        fun value(
            actual: String?,
            name: String? = null
        ): StringConstraints

        @CheckReturnValue
        fun value(
            actual: Int?,
            name: String? = null
        ): IntConstraints

        @CheckReturnValue
        fun value(
            actual: Boolean?,
            name: String? = null
        ): BooleanConstraints

        @CheckReturnValue
        fun <Actual : Collection<ActualItem>, ActualItem> value(
            actual: Actual?,
            name: String? = null
        ): CollectionConstraints<Actual, ActualItem>
    }

    interface RuleConstraints<Actual> {
        fun mustBe(rule: (Actual) -> Boolean): Because<Actual, Unit>

        fun mustNotBe(rule: (Actual) -> Boolean): Because<Actual, Unit>
    }

    interface NullabilityConstraints<Actual> : RuleConstraints<Actual> {
        fun mustBeNull(): Because<Actual, Unit>

        fun mustNotBeNull(): Because<Actual, Unit>
    }

    interface EqualityConstraints<Actual> : NullabilityConstraints<Actual> {
        @CheckReturnValue
        fun mustBeEqual(expected: () -> Actual): Because<Actual, Actual>

        @CheckReturnValue
        fun mustNotBeEqual(expected: () -> Actual): Because<Actual, Actual>
    }

    interface TypeConstraints<Actual> {
        @CheckReturnValue
        fun <Expected : Any> mustBeType(expected: () -> KClass<Expected>): Because<Actual, Actual>

        @CheckReturnValue
        fun <Expected : Any> mustNotBeType(expected: () -> KClass<Expected>): Because<Actual, Actual>
    }

    interface ComparisonConstraints<Actual> : EqualityConstraints<Actual> {
        @CheckReturnValue
        fun <Expected : Comparable<Actual>> mustBeExact(expected: () -> Expected): Because<Actual, Expected>

        @CheckReturnValue
        fun <Expected : Comparable<Actual>> mustNotBeExact(expected: () -> Expected): Because<Actual, Expected>

        @CheckReturnValue
        fun <Expected : Comparable<Actual>> mustBeGreater(expected: () -> Expected): Because<Actual, Expected>

        @CheckReturnValue
        fun <Expected : Comparable<Actual>> mustBeGreaterOrEqual(expected: () -> Expected): Because<Actual, Expected>

        @CheckReturnValue
        fun <Expected : Comparable<Actual>> mustBeLess(expected: () -> Expected): Because<Actual, Expected>

        @CheckReturnValue
        fun <Expected : Comparable<Actual>> mustBeLessOrEqual(expected: () -> Expected): Because<Actual, Expected>
    }

    interface ComparableConstraints<Actual : Comparable<Actual>> : ComparisonConstraints<Actual> {
        @CheckReturnValue
        fun mustBeInRange(expected: () -> ClosedRange<Actual>): Because<Actual, ClosedRange<Actual>>

        @CheckReturnValue
        fun mustNotBeInRange(expected: () -> ClosedRange<Actual>): Because<Actual, ClosedRange<Actual>>
    }

    interface StringConstraints : ComparableConstraints<String> {
        @CheckReturnValue
        fun mustMatch(expected: () -> Pattern): Because<String, Pattern>

        @CheckReturnValue
        fun mustNotMatch(expected: () -> Pattern): Because<String, Pattern>
    }

    interface CollectionConstraints<Actual : Collection<ActualItem>, ActualItem>
        : NullabilityConstraints<Actual> {
        @CheckReturnValue
        fun mustNotBeEmpty(): Because<Actual, Unit>

        @CheckReturnValue
        fun mustBeEmpty(): Because<Actual, Unit>

        @CheckReturnValue
        fun mustHaveSize(expected: () -> Int): Because<Actual, Int>
    }

    interface BooleanConstraints : ComparableConstraints<Boolean> {
        @CheckReturnValue
        fun mustBeTrue(): Because<Boolean, Boolean>

        @CheckReturnValue
        fun mustNotBeTrue(): Because<Boolean, Boolean>

        @CheckReturnValue
        fun mustBeFalse(): Because<Boolean, Boolean>

        @CheckReturnValue
        fun mustNotBeFalse(): Because<Boolean, Boolean>
    }

    interface IntConstraints : ComparableConstraints<Int> {
        @CheckReturnValue
        fun mustBePositive(): Because<Int, Unit>

        @CheckReturnValue
        fun mustNotBePositive(): Because<Int, Unit>

        @CheckReturnValue
        fun mustBeNegative(): Because<Int, Unit>

        @CheckReturnValue
        fun mustNotBeNegative(): Because<Int, Unit>

        @CheckReturnValue
        fun mustBeZero(): Because<Int, Unit>

        @CheckReturnValue
        fun mustNotBeZero(): Because<Int, Unit>
    }

    interface Because<Actual, Expected> : Violation<Actual, Expected> {
        @CheckReturnValue
        fun because(reason: String): Violation<Actual, Expected>
    }

    interface Violation<Actual, Expected> : BuildOrNext {
        @CheckReturnValue
        fun orError(description: (ContractInfo<Actual, Expected>) -> String): BuildOrNext

        @CheckReturnValue
        fun orCustomError(description: (ContractInfo<Actual, Expected>) -> Throwable): BuildOrNext
    }

    interface BuildOrNext : ContractsBuilder, Value

    @CheckReturnValue
    fun build(): Contract
}