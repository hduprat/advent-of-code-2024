import kotlin.math.abs

data class LocationLists(val left: List<Int>, val right: List<Int>)

fun main() {
    fun getLocationLists(input: List<String>): LocationLists {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        input.forEach {
            val splits = it.split(Regex("\\s+"),2)
            leftList.add(splits[0].toInt())
            rightList.add(splits[1].toInt())
        }

        return LocationLists(leftList.sorted(), rightList.sorted())
    }

    fun part1(input: List<String>): Int {

        val locationLists = getLocationLists(input)

        return locationLists.left.foldIndexed(0) {index, acc, elt ->
            val distance = abs(elt-locationLists.right[index])
            distance + acc
        }
    }

    fun part2(input: List<String>): Int {
        val locationLists = getLocationLists(input)

        return locationLists.left.fold(0) { acc, elt ->
            val similarityScore = elt * locationLists.right.count { it == elt }
            similarityScore + acc
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    val input = readInput("Day01")

    check(part1(testInput) == 11)
    part1(input).println()

    check(part2(testInput) == 31)
    part2(input).println()
}
