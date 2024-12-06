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

fun <T> List<T>.isSortedWith(comparator: Comparator<T>): Boolean =
    this.windowed(2).all { (a, b) -> comparator.compare(a, b) < 0 }

val <T> List<T>.middle get() = this[this.size / 2]

typealias IntVector = Pair<Int, Int>
operator fun IntVector.plus(other: IntVector) = this.first + other.first to this.second + other.second