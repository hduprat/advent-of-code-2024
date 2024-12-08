fun computeAntinodes1(a: IntVector, b: IntVector, h: Int, w: Int): List<IntVector> {
    val d = b - a
    return listOf(a - d, b + d).filter { it.inRect(w, h) }
}

fun computeAntinodes2(a: IntVector, b: IntVector, h: Int, w: Int): List<IntVector> {
    val d = b - a
    val antinodes = mutableListOf<IntVector>()

    var antinode = a
    while (antinode.inRect(w, h)) {
        antinodes.add(antinode)

        antinode -= d
    }

    antinode = b
    while (antinode.inRect(w, h)) {
        antinodes.add(antinode)

        antinode += d
    }

    return antinodes
}

class AntennaMap(input: List<String>, val model: (IntVector, IntVector, Int, Int) -> List<IntVector>) {
    private val height = input.size
    private val width = input[0].length

    private val antennas: Map<Char, List<IntVector>>

    val antinodes: List<IntVector>
        get() {
            return antennas.flatMap { (_, nodes) ->
                nodes.combinations().flatMap { (a, b) ->
                    model(a, b, height, width)
                }
            }
        }


    init {
        val initAntennas = mutableMapOf<Char, MutableList<IntVector>>()

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '.' -> Unit
                    in initAntennas -> initAntennas[c]!!.add(x to y)
                    else -> initAntennas[c] = mutableListOf(x to y)
                }
            }
        }

        antennas = initAntennas
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = AntennaMap(input, ::computeAntinodes1)
        return map.antinodes.distinct().size
    }

    fun part2(input: List<String>): Int {
        val map = AntennaMap(input, ::computeAntinodes2)
        return map.antinodes.distinct().size
    }

    val testInput = readInput("Day08_test")
    val input = readInput("Day08")

    check(part1(testInput) == 14)
    part1(input).println()

    check(part2(testInput) == 34)
    part2(input).println()
}
