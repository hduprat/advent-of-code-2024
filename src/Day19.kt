class TowelRack(input: String) {
    private val towels = input.split(", ").sortedBy { it.length }

    private val cache = mutableMapOf<String, Long>()

    init {
        val (littleTowels, otherTowels) = towels.partition { it.length == 1 }
        littleTowels.forEach { cache[it] = 1 }
        otherTowels.forEach { towel ->
            cache[towel] = 1 + towelCombos(towel)
        }
    }

    fun towelCombos(towel: String): Long {
        if (towel in cache) {
            return cache[towel]!!
        } else {
            val eligibleTowels = cache.keys.filter { towel.startsWith(it) }

            val combos = eligibleTowels.sumOf {
                towelCombos(towel.removePrefix(it))
            }

            return combos
        }
    }

    fun designCombos(design: String): Long {
        if (design in cache) {
            return cache[design]!!
        }

        val eligibleTowels = towels.filter { design.startsWith(it) }

        return eligibleTowels.sumOf {
            designCombos(design.removePrefix(it))
        }.also {
            cache[design] = it
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val designs = input.takeLastWhile { it != "" }

        val rack = TowelRack(input[0])
        return designs.count {
            val combos = rack.designCombos(it)
            combos > 0
        }
    }

    /*
     * Au lieu de renvoyer un boolean, renvoyer un int contenant le nombre d'arrangements possibles.
     * Attention, des serviettes de base peuvent aussi être des arrangements ! donc on commence par calculer le nombre d'arrangements possibles pour arriver à chaque serviette
     */
    fun part2(input: List<String>): Long {
        val designs = input.takeLastWhile { it != "" }

        val rack = TowelRack(input[0])
        return designs.sumOf {
            rack.designCombos(it)
        }
    }

    val testInput = readInput("Day19_test")
    val input = readInput("Day19")

    check(part1(testInput) == 6)
    part1(input).println()

    check(part2(testInput) == 16L)
    part2(input).println()
}
