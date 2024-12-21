fun charToDirection(c: Char): Direction =
    when (c) {
        '^' -> Direction.UP
        '>' -> Direction.RIGHT
        'v' -> Direction.DOWN
        '<' -> Direction.LEFT
        else -> throw IllegalStateException("Undefined direction")
    }


sealed class Space {
    data object Crate : Space()
    data object Wall : Space()
}

class Warehouse(input: List<String>) {
    private lateinit var robot: IntVector
    val map = mutableMapOf<IntVector, Space>()
    private val height = input.size
    private val width = input[0].length

    init {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '@' -> robot = x to y
                    '#' -> map[x to y] = Space.Wall
                    'O' -> map[x to y] = Space.Crate
                }
            }
        }
    }

    fun move(d: Direction) {
        val position = robot + d.vector
        if (position !in map) {
            robot = position
            return
        }
        var p = position
        while (map[p] is Space.Crate) {
            p += d.vector
        }
        if (map[p] == Space.Wall) {
            return
        }
        map.remove(position)
        map[p] = Space.Crate
        robot = position
    }

    val crateCoords: List<Int> get() = map.filterValues { it is Space.Crate }.keys.map { (x, y) -> 100 * y + x }

    override fun toString(): String =
        (0 until height).joinToString("\n") { y ->
            (0 until width).joinToString("") { x ->
                if (x to y == robot) "@" else when (map[x to y]) {
                    is Space.Wall -> "#"
                    Space.Crate -> "O"
                    else -> "."
                }
            }
        }
}

class BigWarehouse(input: List<String>) {
    private lateinit var robot: IntVector
    val map = mutableMapOf<IntVector, Space>()
    private val height = input.size
    private val width = 2 * input[0].length

    init {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    '@' -> robot = 2 * x to y
                    '#' -> {
                        map[2 * x to y] = Space.Wall
                        map[2 * x + 1 to y] = Space.Wall
                    }

                    'O' -> map[2 * x to y] = Space.Crate
                }
            }
        }
    }

    private fun crateAt(p: IntVector): Pair<IntVector, IntVector>? {
        if (map[p] is Space.Crate) return p to (p + (1 to 0))
        if (map[p + (-1 to 0)] is Space.Crate) return (p + (-1 to 0)) to p
        return null
    }

    private fun isEmptySpaceAt(p: IntVector): Boolean {
        if (crateAt(p) != null) return false
        return p !in map
    }

    private fun pushCrateAt(p: IntVector, d: Direction): Boolean {
        // on récupère les 2 côtés de la caisse (si pas de caisse, on arrête)
        val crate = crateAt(p) ?: return false
        val (left, right) = crate
        println("Push crate at $left and $right to the $d")

        // Est-ce qu'on va essayer de pousser contre un mur ?
        if (map[left + d.vector] is Space.Wall || map[right + d.vector] is Space.Wall) {
            println("It's against a wall :(")
            return false
        }

        // On va checker s'il y a des caisses des deux côtés
        // pourquoi checker les deux côtés ? dans ce genre de cas
        //[][]  @↓
        // []   []
        // @↑  [][]
        val nextLeftCrate = crateAt(left + d.vector)
        val nextRightCrate = crateAt(right + d.vector)

        // Sinon, on pousse les deux caisses.
        // On vérifie qu'on va pas faire une boucle infinie en poussant la caisse de base
        // @→[][] ou [][]←@
        var shouldPush = false
        if (nextLeftCrate != crate) {
            println("Next crate to the left: $nextLeftCrate")
            shouldPush = nextLeftCrate == null || pushCrateAt(left + d.vector, d)

        }
        if (nextRightCrate != crate) {
            println("Next crate to the right: $nextRightCrate")
            shouldPush = shouldPush || nextRightCrate == null || pushCrateAt(right + d.vector, d)

        }

        // Si les deux ne sont pas des caisses :
        if (shouldPush) {
            println("Nothing at ${left + d.vector} and ${right + d.vector}. Let's move the crate!")

            // INCR ! La caisse se déplace.
            map.remove(left)
            map[left + d.vector] = Space.Crate

            return true
        }

        return false
    }

    fun move(d: Direction) {
        val position = robot + d.vector
        if (isEmptySpaceAt(position)) {
            robot = position
            return
        }
        if (pushCrateAt(position, d))
            robot = position
    }

    val crateCoords: List<Int> get() = map.filterValues { it is Space.Crate }.keys.map { (x, y) -> 100 * y + x }

    override fun toString(): String =
        (0 until height).joinToString("\n") { y ->
            var isCrate = false
            (0 until width).joinToString("") { x ->
                if (x to y == robot) "@" else when (map[x to y]) {
                    is Space.Wall -> "#"
                    Space.Crate -> {
                        isCrate = true
                        "["
                    }

                    else -> {
                        val c = if (isCrate) "]" else "."
                        isCrate = false
                        c
                    }
                }
            }
        }
}

fun main() {
    fun part1(input: List<String>): Int {
        val (mapInput, routeInput) = input.split("")
        val warehouse = Warehouse(mapInput)
        val route = routeInput.joinToString("").map(::charToDirection)

        route.forEach { warehouse.move(it) }

        return warehouse.crateCoords.sum()
    }

    fun part2(input: List<String>): Int {
        val (mapInput, routeInput) = input.split("")

        val warehouse = BigWarehouse(mapInput)
        val route = routeInput.joinToString("").map(::charToDirection)

        route.forEach { d ->
            warehouse.println()
            warehouse.move(d)
        }
        warehouse.println()


        return warehouse.crateCoords.sum()
    }

    val testInput = readInput("Day15_test")
    val largerTestInput = readInput("Day15_test_larger")
    val input = readInput("Day15")

    check(part1(testInput) == 2028)
    check(part1(largerTestInput) == 10092)
    part1(input).println()

    check(part2(largerTestInput) == 9021)
    part2(input).println()
}
