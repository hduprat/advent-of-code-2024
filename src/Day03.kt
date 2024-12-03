import java.lang.Exception

sealed class Operation {
    data class Multiplier(val a: Int, val b: Int) : Operation()
    data object Do : Operation()
    data object Dont : Operation()
}

class Program(input: List<String>) {
    private val multiplierPattern = """(mul)\((\d{1,3}),(\d{1,3})\)"""
    private val doPattern = """(do)\(\)"""
    private val dontPattern = """(don't)\(\)"""

    private val operations: List<Operation>

    init {
        val globalRegex = "$multiplierPattern|$doPattern|$dontPattern".toRegex()
        operations = input.flatMap { line ->
            globalRegex.findAll(line).map { result ->
                val groups = result.groupValues.filter { it != "" }
                when (val op = groups[1]) {
                    "do" -> Operation.Do
                    "don't" -> Operation.Dont
                    "mul" -> Operation.Multiplier(groups[2].toInt(), groups[3].toInt())
                    else -> throw IllegalStateException("$op operation should not be present")
                }
            }

        }
    }

    val sumOfAllMults = operations.filterIsInstance<Operation.Multiplier>().sumOf { (a, b) -> a * b }

    val sumOfAllEnabledMults: Int
        get() {
            var enabled = true
            var total = 0
            for (op in operations) {
                when (op) {
                    is Operation.Do -> enabled = true
                    is Operation.Dont -> enabled = false
                    is Operation.Multiplier -> if (enabled) total += op.a * op.b
                }
            }

            return total
        }
}

fun main() {
    fun part1(input: List<String>): Int {
        return Program(input).sumOfAllMults
    }

    fun part2(input: List<String>): Int {
        return Program(input).sumOfAllEnabledMults
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")

    check(part1(testInput) == 161)
    part1(input).println()

    check(part2(testInput) == 48)
    part2(input).println()
}
