package aoc2025

import utils.Coordinate
import utils.x
import utils.y
import java.io.File
import kotlin.math.abs


fun main() {
    val redTiles: List<Coordinate> = File("./src/main/resources/aoc2025/day9_input.txt").readLines().map { line ->
        line.split(",").map(String::toInt).let { it.first() to it.last()}
    }
    val redTilePairs = redTiles.indices.flatMap { i ->
        (i+1..<redTiles.size).map { j ->
            redTiles[i] to redTiles[j]
        }
    }
    val answerPart1 = redTilePairs.maxOf { (tile1, tile2) ->
        calculateSurfaceArea(tile1, tile2)
    }
    println("Answer part 1: $answerPart1")


    fun Pair<Coordinate, Coordinate>.isValid() = validatePacman() && redTiles.none { redTile ->
        (redTile.x in first.x+1..<second.x || redTile.x in second.x+1..<first.x) &&
                (redTile.y in first.y+1..<second.y || redTile.y in second.y+1..<first.y)
    }

    val answerPart2 = redTilePairs.filter { it.isValid() }.maxOf { (tile1, tile2) ->
        calculateSurfaceArea(tile1, tile2)
    }
    println("Answer part 2: $answerPart2")
}

private fun calculateSurfaceArea(tile1: Coordinate, tile2: Coordinate): Long =
    (abs(tile1.x - tile2.x) + 1).toLong() * (abs(tile1.y - tile2.y) + 1)

fun Pair<Coordinate, Coordinate>.validatePacman() = this.let { (tile1, tile2) ->
    tile1.y <= pacmanLowerHalf && tile2.y <= pacmanLowerHalf || tile1.y >= pacmanUpperHalf && tile2.y >= pacmanUpperHalf
}

const val pacmanLowerHalf = 48525
const val pacmanUpperHalf = 50228
