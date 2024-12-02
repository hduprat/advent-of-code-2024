import kotlin.math.abs
import kotlin.math.sign

fun main() {

    class Report(val levels: List<Int>) {

        constructor (input: String) : this(input.split(" ").map { it.toInt() })

        val isSafe: Boolean
            get() {
                var variationSign = 0
                for ((first, second) in levels.windowed(2)) {
                    val diff = second - first
                    when {
                        diff == 0 -> return false
                        abs(diff) > 3 -> return false
                        variationSign == 0 -> variationSign = diff.sign
                        diff.sign != variationSign -> return false
                    }
                }

                return true
            }

        val isKindaSafe: Boolean
            get() {
                if (this.isSafe) return true

                for (i in levels.indices) {
                    val otherLevels = levels.toMutableList()
                    otherLevels.removeAt(i)
                    if (Report(otherLevels).isSafe) return true
                }

                return false
            }
    }

    fun part1(input: List<String>): Int {
        return input.count { Report(it).isSafe }
    }

    fun part2(input: List<String>): Int {
        return input.count { Report(it).isKindaSafe }
    }

    val testInput = readInput("Day02_test")
    val input = readInput("Day02")

    check(part1(testInput) == 2)
    part1(input).println()

    check(part2(testInput) == 4)
    part2(input).println()
}
