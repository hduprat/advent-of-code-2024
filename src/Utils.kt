import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/*
 * List overloads
 */
fun <T> List<T>.isSortedWith(comparator: Comparator<T>): Boolean =
    this.windowed(2).all { (a, b) -> comparator.compare(a, b) < 0 }

fun <T> List<T>.combinations(): List<Pair<T, T>> = this.flatMapIndexed { i, elt ->
    this.slice(i + 1 until this.size).map { elt to it }
}

val <T> List<T>.middle get() = this[this.size / 2]

/*
 * Long overloads
 */
fun Long.concat(l: Long): Long = "$this$l".toLong()

/*
 * IntVector
 */
typealias IntVector = Pair<Int, Int>

operator fun IntVector.plus(other: IntVector): IntVector = this.first + other.first to this.second + other.second
operator fun IntVector.minus(other: IntVector): IntVector = this.first - other.first to this.second - other.second
operator fun IntVector.times(n: Int): IntVector = n * this.first to n * this.second
operator fun Int.times(vec: IntVector): IntVector = vec.times(this)

fun IntVector.inRect(height: Int, width: Int) = this.first in 0 until width && this.second in 0 until height

enum class Direction(val vector: IntVector) {
    UP(0 to -1),
    RIGHT(1 to 0),
    DOWN(0 to 1),
    LEFT(-1 to 0),
}

/*
 * Grid
 */
typealias Grid<T> = List<List<T>>

fun <T> Grid<T>.at(point: IntVector): T {
    val (x, y) = point
    return this[y][x]
}

fun <T> Grid<T>.atOrNull(point: IntVector): T? {
    return try {
        this.at(point)
    } catch (exc: Exception) {
        null
    }
}