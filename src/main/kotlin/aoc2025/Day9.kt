package aoc2025

import utils.x
import utils.y
import java.io.File
import kotlin.math.abs


fun main() {
    val redTiles = File("./src/main/resources/aoc2025/day9_input.txt").readLines().map { line ->
        line.split(",").map(String::toInt).let { it.first() to it.last()}
    }
    val answerPart1 = redTiles.indices.flatMap { i ->
        (i+1..<redTiles.size).map { j ->
            redTiles[i] to redTiles[j]
        }
    }.maxOf { (tile1, tile2) ->
        (abs(tile1.x - tile2.x) + 1).toLong() * (abs(tile1.y - tile2.y) + 1)
    }
    println("Answer part 1: $answerPart1")
}
