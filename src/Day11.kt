fun <K> MutableMap<K, Long>.addOrPut(key: K, elt: Long) = this.put(key, this.getOrDefault(key, 0) + elt)

class StoneLine(input: String) {
    private var stoneMap = input.split(" ").groupBy { it }.mapValues { it.value.size.toLong() }

    fun blink() {
        val newMap = mutableMapOf<String, Long>()

        stoneMap.forEach { (number, occurrences) ->
            when {
                number == "0" -> newMap.addOrPut("1", occurrences)
                number.length % 2 == 0 -> {
                    val (first, second) = number.take(number.length / 2) to number.substring(number.length / 2)
                    newMap.addOrPut(first, occurrences)
                    newMap.addOrPut(second.toLong().toString(), occurrences)
                }

                else -> newMap.addOrPut("${number.toLong() * 2024}", occurrences)
            }
        }

        stoneMap = newMap
    }

    fun countStones(): Long {
        return stoneMap.map { (_, n) -> n }.sum()
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val stoneLine = StoneLine(input[0])
        for (i in 1..25) {
            stoneLine.blink()
        }
        return stoneLine.countStones()
    }

    fun part2(input: List<String>): Long {
        val stoneLine = StoneLine(input[0])
        for (i in 1..75) {
            stoneLine.blink()
        }
        return stoneLine.countStones()
    }

    val testInput = readInput("Day11_test")
    val input = readInput("Day11")

    check(part1(testInput) == 55312L)
    part1(input).println()

    part2(input).println()
}
