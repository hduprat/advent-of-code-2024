import java.util.Objects

class UniqueTriple<T>(private val first: T, private val second: T, private val third: T) {
    private val set = setOf(first, second, third)
    fun toSet() = set

    override fun equals(other: Any?): Boolean {
        if (other !is UniqueTriple<*>) return false
        return set == other.set
    }

    operator fun component1(): T = first
    operator fun component2(): T = second
    operator fun component3(): T = third

    operator fun contains(elt: T): Boolean = elt in set
    override fun toString(): String = "${Triple(first, second, third)}"
    override fun hashCode(): Int = Objects.hash(set)
}

class LAN(input: List<String>) {
    private val network = buildMap {
        input.map { line ->
            val (a, b) = line.split("-").zipWithNext().single()
            compute(a) { _, value: Set<String>? -> if (value == null) setOf(b) else value + b }
            compute(b) { _, value: Set<String>? -> if (value == null) setOf(a) else value + a }
        }
    }

    val triplets: Set<UniqueTriple<String>> = buildSet {
        network.forEach { (refA, others) ->
            others.forEach { refB ->
                val nodeB = network[refB] ?: emptySet()
                nodeB.intersect(others).forEach { add(UniqueTriple(refA, refB, it)) }
            }
        }
    }

    private val allCombos = mutableSetOf<Set<String>>()
    private val clusterSet = mutableSetOf<Set<String>>()

    private fun expandCluster(cluster: Set<String>) {
        if (cluster in allCombos) return
        allCombos += cluster
        val moreElements = network.filterValues { nodes -> nodes.containsAll(cluster) }.keys
        if (moreElements.isEmpty()) clusterSet += cluster else moreElements.forEach { expandCluster(cluster + it) }
    }

    val clusters: Set<Set<String>>
        get() {
            if (clusterSet.isEmpty()) {
                network.keys.forEach { expandCluster(setOf(it)) }
            }
            return clusterSet
        }

    override fun toString(): String =
        network.keys.joinToString("\n") {
            "$it -> ${network[it]?.joinToString() ?: "nothing"}"
        }

}

fun main() {
    fun part1(input: List<String>): Int {
        val lan = LAN(input)
        return lan.triplets.count { triplet -> triplet.toSet().any { it.startsWith('t') } }
    }

    fun part2(input: List<String>): String {
        val lan = LAN(input)
        val biggestCluster = lan.clusters.maxBy { it.size }
        return biggestCluster.sorted().joinToString(",")
    }

    val testInput = readInput("Day23_test")
    val input = readInput("Day23")

    check(part1(testInput) == 7)
    part1(input).println()

    check(part2(testInput) == "co,de,ka,ta")
    part2(input).println()
}
