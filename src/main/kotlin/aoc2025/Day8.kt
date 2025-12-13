package aoc2025

import java.io.File
import kotlin.time.measureTime

fun main() {
    val testInput = false
    val boxCoordinates = File("./src/main/resources/aoc2025/day8_${if (testInput) "test_" else ""}input.txt").readLines()
        .map { it.split(",").map(String::toInt).toCoordinate() }
    val numberOfConnections = if (testInput) 10 else 1000
    measureTime {
        val boxPairs = boxCoordinates.flatMap { box1 ->
            boxCoordinates.mapNotNull { box2 -> setOf(box1, box2).takeIf { it.size > 1 } }
        }.toSet().map { it.toList() }
            .sortedBy { (box1, box2) -> box1 distanceTo box2 }
        val circuits = boxPairs.take(numberOfConnections).fold(emptyList<List<`3DCoordinate`>>()) { circuits, newPair ->
            updateCircuits(newPair, circuits)
        }
        val answerPart1 = circuits.map { it.size }.sortedDescending().take(3).reduce(Int::times)
        println("Answer part 1: $answerPart1")

        val lastBoxPair = boxPairs.fold(emptyList<List<`3DCoordinate`>>() to boxPairs.first()) { (circuits, lastBoxPair), newPair ->
            if (circuits.firstOrNull()?.size == boxCoordinates.size) circuits to lastBoxPair
            else updateCircuits(newPair, circuits) to newPair
        }.second
        println("Answer part 2: ${lastBoxPair.first().x * lastBoxPair.last().x}")
    }.also { println("Time taken: $it") }

}

private fun updateCircuits(
    newPair: List<`3DCoordinate`>,
    circuits: List<List<`3DCoordinate`>>,
): List<List<`3DCoordinate`>> {
    val (box1, box2) = newPair
    val (set1, set2) = newPair.map { box -> circuits.find { it.contains(box) } }
    return when {
        (set1 ?: set2) == null -> circuits + listOf(newPair)
        set1 != null && set2 == null -> circuits.addElementToSublist(set1, box2)
        set1 == null && set2 != null -> circuits.addElementToSublist(set2, box1)
        set1 != null && set1 == set2 -> circuits
        set1 != null && set2 != null -> circuits.filterNot { circuit -> newPair.any { circuit.contains(it) } } + listOf(
            set1 + set2
        )

        else -> throw IllegalArgumentException("Won't happen")
    }
}

fun<T> List<List<T>>.addElementToSublist(sublist: List<T>, element: T) = filter { it != sublist }.plusElement(sublist + element)

data class `3DCoordinate`(val x: Int, val y: Int, val z: Int)

fun List<Int>.toCoordinate() = `3DCoordinate`(this[0], this[1], this[2])

infix fun `3DCoordinate`.distanceTo(other: `3DCoordinate`) =
    (x - other.x).squared() + (y - other.y).squared() + (z - other.z).squared()

fun Int.squared() = this.toLong() * this