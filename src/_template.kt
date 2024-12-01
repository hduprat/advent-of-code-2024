fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("DayXX_test")
    val input = readInput("DayXX")

    check(part1(testInput) == 1)
    part1(input).println()

    check(part2(testInput) == 1)
    part2(input).println()
}
