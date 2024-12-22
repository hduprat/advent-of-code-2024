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

fun <T> List<T>.split(elt: T): List<List<T>>{
    val index = this.indexOf(elt)
    if (index < 0) return listOf(this)
    val list = mutableListOf<List<T>>()
    list.add(this.take(index))
    list.addAll(this.slice(index+1 until this.size).split(elt))
    return list.toList()
}

val <T> List<T>.middle get() = this[this.size / 2]

/*
 * Long overloads
 */
fun Long.concat(l: Long): Long = "$this$l".toLong()


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

fun parseGrid(input: List<String>, op: (IntVector, Char) -> Unit) {
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            op(x to y, c)
        }
    }
}