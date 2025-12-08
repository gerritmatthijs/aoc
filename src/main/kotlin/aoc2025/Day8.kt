package aoc2025

import utils.print
import java.io.File
import kotlin.time.measureTime

fun main() {
    val testInput = false
    val boxCoordinates = File("./src/main/resources/aoc2025/day8_${if (testInput) "test_" else ""}input.txt").readLines()
        .map { it.split(",").map(String::toInt).toCoordinate() }
    val numberOfConnections = if (testInput) 10 else 1000
    measureTime {
        val closestBoxPairs = boxCoordinates.flatMap { box1 ->
            boxCoordinates.mapNotNull { box2 -> setOf(box1, box2).takeIf { it.size > 1 } }
        }.toSet().map { it.toList() }
            .sortedBy { (box1, box2) -> box1 distanceTo box2 }.take(numberOfConnections)
        val circuits = closestBoxPairs.fold(emptyList<List<Coordinate>>()) { circuits, newPair ->
            val (box1, box2) = newPair
            val (set1, set2) = newPair.map { box -> circuits.find { it.contains(box) } }
            when {
                (set1 ?: set2) == null -> circuits + listOf(newPair)
                set1 != null && set2 == null -> circuits.addElementToSublist(set1, box2)
                set1 == null && set2 != null -> circuits.addElementToSublist(set2, box1)
                set1 != null && set1 == set2 -> circuits
                set1 != null && set2 != null -> circuits.filterNot { circuit -> newPair.any { circuit.contains(it)} } + listOf(set1 + set2)
                else -> throw IllegalArgumentException("Won't happen")
            }
        }
        val answerPart1 = circuits.map { it.size }.sortedDescending().take(3).reduce(Int::times)
        println("Answer part 1: $answerPart1")
    }.also { println("Time taken: $it") }
}

fun<T> List<List<T>>.addElementToSublist(sublist: List<T>, element: T) = filter { it != sublist }.plusElement(sublist + element)

data class Coordinate(val x: Int, val y: Int, val z: Int)

fun List<Int>.toCoordinate() = Coordinate(this[0], this[1], this[2])

infix fun Coordinate.distanceTo(other: Coordinate) =
    (x - other.x).squared() + (y - other.y).squared() + (z - other.z).squared()

fun Int.squared() = this.toLong() * this