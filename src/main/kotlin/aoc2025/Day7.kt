package aoc2025

import utils.toGrid
import utils.y
import java.io.File

fun main() {
    val manifold = File("./src/main/resources/aoc2025/day7_input.txt").readText().toGrid()
    val startingBeamPosition = manifold.indexOf('S')!!.y
    val answerPart1 = (0..<manifold.vSize).fold(setOf(startingBeamPosition) to 0) { (beams, splitterCount), rowIndex ->
        val newBeams = beams.flatMap { beam ->
            if (manifold[rowIndex, beam] == '^') listOf(beam - 1, beam + 1) else listOf(beam)
        }
        newBeams.toSet() to splitterCount + newBeams.size - beams.size
    }.second

    println("Answer part 1: $answerPart1")

    val aggregatedBeams = (0..<manifold.vSize).fold(mapOf(startingBeamPosition to 1L)) { beams, rowIndex ->
        val newBeams = beams.flatMap { (beam, count) ->
            (if (manifold[rowIndex, beam] == '^') listOf(beam - 1, beam + 1) else listOf(beam)).map { it to count }
        }
        newBeams.groupBy({ it.first }) { it.second }.mapValues { (_, values) ->
            values.sum()
        }
    }
    println("Answer part 2: ${aggregatedBeams.values.sum()}")
}