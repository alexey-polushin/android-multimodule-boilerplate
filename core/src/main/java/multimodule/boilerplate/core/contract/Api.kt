package multimodule.boilerplate.core.contract

/**
 * Example:
 *
 *      fun divide(
 *          numerator: Int,
 *          denominator: Int
 *      ): Int {
 *          contracts {
 *              it
 *                  .value(denominator, "denominator")
 *                  .mustNotBeZero()
 *                  .because("denominator cannot be zero")
 *          }
 *
 *          return (numerator / denominator).also { result ->
 *              contracts {
 *                  it
 *                      .value(result * denominator, "result * denominator")
 *                      .mustBeLessOrEqual { numerator }
 *                      .because("that's how division is defined")
 *              }
 *          }
 *      }
 */
inline fun contracts(creator: (ContractsBuilder.Start) -> ContractsBuilder) =
    creator(ImmutableContractsBuilder.start)
        .build()
        .assert()

/**
 * Example:
 *      fun divide(
 *          numerator: Int,
 *          denominator: Int
 *      ): Int = contracted(
 *          pre = { contracts ->
 *              contracts
 *                  .value(denominator, "denominator")
 *                  .mustNotBeZero()
 *                  .because("denominator cannot be zero")
 *          },
 *          post = { contracts, result ->
 *              contracts
 *                  .value(result * denominator, "result * denominator")
 *                  .mustBeLessOrEqual { numerator }
 *                  .because("that's how division is defined")
 *          }
 *      ) {
 *          numerator / denominator
 *      }
 */
inline fun <Result> contracted(
    pre: (ContractsBuilder.Start) -> ContractsBuilder = { ImmutableContractsBuilder.empty },
    post: (ContractsBuilder.Start, Result) -> ContractsBuilder = { _, _ -> ImmutableContractsBuilder.empty },
    crossinline block: () -> Result
): Result {
    pre(ImmutableContractsBuilder.start).build().assert()

    val result = block()

    post(ImmutableContractsBuilder.start, result).build().assert()

    return result
}

fun unreachable(): Nothing = throw UnreachableException()