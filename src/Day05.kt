class PageComparator(rules: List<String>) : Comparator<Int> {
    private val ruleMap: Map<Int, List<Int>>

    init {
        val tempMap = mutableMapOf<Int, MutableList<Int>>()
        rules.forEach { rule ->
            val (a, b) = rule.split("|").map(String::toInt)
            val entry = tempMap[a]
            if (entry == null) tempMap[a] = mutableListOf(b)
            else entry.add(b)
        }
        ruleMap = tempMap
    }

    override fun compare(o1: Int?, o2: Int?): Int = if (ruleMap[o1]?.contains(o2) == true) -1 else 1
}

fun getUpdatesPartition(input: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
    val separatorIndex = input.indexOf("")
    val rulesList = input.slice(0 until separatorIndex)
    val updates = input.slice(separatorIndex + 1 until input.size).map { it.split(",").map(String::toInt) }

    val pageComparator = PageComparator(rulesList)
    val (correctlyOrderedUpdates, incorrectlyOrderedUpdates) = updates.partition { it.isSortedWith(pageComparator) }

    return correctlyOrderedUpdates to incorrectlyOrderedUpdates.map { it.sortedWith(pageComparator) }
}

fun main() {
    fun part1(input: List<String>): Int {
        val (correctlyOrderedUpdates) = getUpdatesPartition(input)

        return correctlyOrderedUpdates.sumOf { it.middle }
    }

    fun part2(input: List<String>): Int {
        val (_, reorderedUpdates) = getUpdatesPartition(input)

        return reorderedUpdates.sumOf { it.middle }
    }

    val testInput = readInput("Day05_test")
    val input = readInput("Day05")

    check(part1(testInput) == 143)
    part1(input).println()

    check(part2(testInput) == 123)
    part2(input).println()
}
