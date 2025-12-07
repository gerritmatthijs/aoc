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
}