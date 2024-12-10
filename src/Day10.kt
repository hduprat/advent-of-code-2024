fun getMap(input: List<String>): Grid<Int> {
    return input.map { it.toList().map(Char::digitToInt) }
}

fun findPotentialTrailheads(map: Grid<Int>): List<IntVector> {
    val zeroes = mutableListOf<IntVector>()
    map.forEachIndexed { y: Int, line: List<Int> ->
        line.forEachIndexed { x, height ->
            if (height == 0) zeroes.add(x to y)
        }
    }

    return zeroes.toList()
}

fun getNines(coords: IntVector, map: Grid<Int>): Set<IntVector> {
    val nines = mutableSetOf<IntVector>()
    try {
        val height = map.at(coords)
        if (height == 9) {
            nines.add(coords)
            return nines
        }

        val nextHeight = height + 1

        Direction.entries.forEach { direction ->
            val nextCoords = coords + direction.vector
            if (map.atOrNull(nextCoords) == nextHeight) {
                nines.addAll(getNines(nextCoords, map))
            }
        }

        return nines
    } catch (exception: Exception) {
        return emptySet()
    }
}

fun getRating(coords: IntVector, map: Grid<Int>): Int {
    try {
        val height = map.at(coords)
        if (height == 9) {
            return 1
        }

        var rating = 0

        val nextHeight = height + 1

        Direction.entries.forEach { direction ->
            val nextCoords = coords + direction.vector
            if (map.atOrNull(nextCoords) == nextHeight) {
                rating += getRating(nextCoords, map)
            }
        }

        return rating
    } catch (exception: Exception) {
        return 0
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = getMap(input)
        val zeroes = findPotentialTrailheads(map)
        return zeroes.sumOf { getNines(it, map).size }
    }

    fun part2(input: List<String>): Int {
        val map = getMap(input)
        val zeroes = findPotentialTrailheads(map)
        return zeroes.sumOf { getRating(it, map) }
    }

    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    check(part1(testInput) == 36)
    part1(input).println()

    check(part2(testInput) == 81)
    part2(input).println()
}
