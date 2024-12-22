import kotlin.math.abs

/*
 * IntVector
 */
typealias IntVector = Pair<Int, Int>

operator fun IntVector.plus(other: IntVector): IntVector = this.first + other.first to this.second + other.second
operator fun IntVector.minus(other: IntVector): IntVector = this.first - other.first to this.second - other.second
operator fun IntVector.times(n: Int): IntVector = n * this.first to n * this.second
operator fun Int.times(vec: IntVector): IntVector = vec.times(this)

fun IntVector.inRect(height: Int, width: Int) = this.first in 0 until width && this.second in 0 until height
val IntVector.norm: Int get() = abs(this.first) + abs(this.second)


enum class Direction(val vector: IntVector) {
    UP(0 to -1),
    RIGHT(1 to 0),
    DOWN(0 to 1),
    LEFT(-1 to 0),
}
val Direction.next: Direction get() = Direction.entries[(this.ordinal + 1) % Direction.entries.size]
val Direction.previous: Direction get() = Direction.entries[(this.ordinal - 1).mod(Direction.entries.size)]
