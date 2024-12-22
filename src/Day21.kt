/*
 * Conception :
 * Un robot est forcément piloté par un keypad.
 * On peut mettre en cache les mouvements nécessaires pour aller d'une touche à une autre
 * La méthode la plus pratique entre deux touches est de maxer une touche puis l'autre.
 * (exemple : ^>^> est moins efficace à piloter que ^^>> ou >>^^, tout dépend de la touche actuelle)
 * Toujours finir par la touche A
 *
 * Pour optimiser :
 * Toujours faire un max de lignes droites.
 */

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("Day21_test")
    val input = readInput("Day21")

    check(part1(testInput) == 126384)
    part1(input).println()

    check(part2(testInput) == 1)
    part2(input).println()
}
