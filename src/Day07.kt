class EquationSolver(private val operations: List<(Long, Long) -> Long>) {
    private fun findSolvable(result: Long, operands: List<Long>, acc: Long): Boolean {
        if (operands.isEmpty()) {
            return acc == result
        }

        val restOfOperands = operands.slice(1 until operands.size)
        val number = operands[0]

        return operations.fold(false) { conclusion, operation ->
            val hasBeenSolved = findSolvable(result, restOfOperands, operation(acc, number))
            if (hasBeenSolved) return@findSolvable true
            conclusion
        }
    }

    fun isSolvable(result: Long, operands: List<Long>): Boolean {
        val restOfOperands = operands.slice(1 until operands.size)
        val number = operands[0]

        return findSolvable(result, restOfOperands, number)
    }
}

fun main() {
    fun parse(input: String): Pair<Long, List<Long>> {
        val (result, operands) = input.split(": ")
        return result.toLong() to operands.split(" ").map(String::toLong)
    }

    fun part1(input: List<String>): Long {
        val solver = EquationSolver(listOf(Long::plus, Long::times))
        val parsedInput = input.map(::parse)

        return parsedInput.filter { (result, operands) -> solver.isSolvable(result, operands) }.sumOf { it.first }
    }

    fun part2(input: List<String>): Long {
        val solver = EquationSolver(listOf(Long::plus, Long::times, Long::concat))
        val parsedInput = input.map(::parse)

        return parsedInput.filter { (result, operands) -> solver.isSolvable(result, operands) }.sumOf { it.first }
    }

    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    check(part1(testInput) == 3749L)
    part1(input).println()

    check(part2(testInput) == 11387L)
    part2(input).println()

}
