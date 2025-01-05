package aoc2024

import utils.*
import utils.KingDirections.*
import java.io.File
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    val codes = File("./src/main/resources/day21_input.txt").readLines().map { it.stripWindowsLineFeed() }

    measureTime {
        val finalVectorPart1 = (1..2).fold(initialVector) { vector, _ -> vector.getNextVector() }

        val answerPart1 = codes.sumOf { code ->
            code.calculateStepsForCode(finalVectorPart1) * code.dropLast(1).toInt()
        }
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken: $it") }

    measureTime {
        val finalVectorPart2 = (1..25).fold(initialVector) { vector, _ -> vector.getNextVector() }

        val answerPart2 = codes.sumOf { code ->
            code.calculateStepsForCode(finalVectorPart2) * code.dropLast(1).toInt()
        }
        println("Answer part 2: $answerPart2")
    }.also { println("Time taken: $it") }
}

fun String.calculateStepsForCode(vector: (KingDirections) -> Long) = ("A$this").toList().windowed(2).sumOf { (start, end) ->
    vector.calculateFinalKeypadSteps(start, end)
}


fun ((KingDirections) -> Long).calculateFinalKeypadSteps(start: Char, end: Char): Long =
    (start.getCoordinate() to end.getCoordinate()).let { (from, to) -> (to.x - from.x) to (to.y - from.y) }.let { d ->
        when {
            d.x > 0 && d.y == 0 -> this(DOWN) + d.x - 1
            d.x < 0 && d.y == 0 -> this(UP) - d.x - 1
            d.x == 0 && d.y > 0 -> this(RIGHT) + d.y - 1
            d.x == 0 && d.y < 0 -> this(LEFT) - d.y - 1
            d.x > 0 && d.y < 0 -> this.getDownLeftDiag() + d.x - d.y - 2
            d.x < 0 && d.y > 0 -> this.getUpRightDiag() - d.x + d.y - 2
            d.x > 0 && d.y > 0 && start in listOf('1', '4', '7') && end in listOf('0', 'A') -> this(RIGHTDOWN) + d.x + d.y - 2
            d.x > 0 && d.y > 0 -> this.getDownRightDiag() + d.x + d.y - 2
            d.x < 0 && d.y < 0 && start in listOf('0', 'A') && end in listOf('1', '4', '7') -> this(UPLEFT) - d.x - d.y - 2
            d.x < 0 && d.y < 0 -> this.getUpLeftDiag() - d.x - d.y - 2
            else -> throw Exception("Wrong start, end: $start, $end")
        }
    }

fun Char.getCoordinate(): Coordinate = when (this) {
    '7' -> 0 to 0
    '8' -> 0 to 1
    '9' -> 0 to 2
    '4' -> 1 to 0
    '5' -> 1 to 1
    '6' -> 1 to 2
    '1' -> 2 to 0
    '2' -> 2 to 1
    '3' -> 2 to 2
    '0' -> 3 to 1
    'A' -> 3 to 2
    else -> throw Exception("Unknown character: $this")
}

val initialVector = { direction: KingDirections -> if (direction in listOf(RIGHT, UP, LEFT, DOWN)) 2L else 3L }

fun ((KingDirections) -> Long).getNextVector() = mapOf(
    RIGHT to this(DOWN) + this(UP),
    UP to this(LEFT) + this(RIGHT),
    LEFT to this(DOWNLEFT) + this(RIGHTUP) + 2,
    DOWN to this.getDownLeftDiag() + this.getUpRightDiag(),
    UPRIGHT to this(LEFT) + this.getDownRightDiag() + this(UP),
    RIGHTUP to this(DOWN) + this.getUpLeftDiag() + this(RIGHT),
    DOWNRIGHT to this.getDownLeftDiag() + this(RIGHT) + this(UP),
    RIGHTDOWN to this(DOWN) + this(LEFT) + this.getUpRightDiag(),
    LEFTUP to this(DOWNLEFT) + this(RIGHTUP) + this(RIGHT) + 1,
    UPLEFT to this(LEFT) + this(DOWNLEFT) + this(RIGHTUP) + 1,
    LEFTDOWN to this(DOWNLEFT) + this(RIGHT) + this.getUpRightDiag() + 1,
    DOWNLEFT to this.getDownLeftDiag() + this(LEFT) + this(RIGHTUP) + 1
).let {
    { direction: KingDirections -> it[direction]!!}
}

// When the order doesn't matter
fun ((KingDirections) -> Long).getDownLeftDiag() = min(this(DOWNLEFT), this(LEFTDOWN))
fun ((KingDirections) -> Long).getDownRightDiag() = min(this(DOWNRIGHT), this(RIGHTDOWN))
fun ((KingDirections) -> Long).getUpRightDiag() = min(this(UPRIGHT), this(RIGHTUP))
fun ((KingDirections) -> Long).getUpLeftDiag() = min(this(UPLEFT), this(LEFTUP))

fun ((KingDirections) -> Long).print() = KingDirections.entries.forEach {
    println("$it: ${this(it)}")
}
