package aoc2024

import utils.Coordinate
import utils.getAdjacents
import utils.toGrid
import java.io.File

fun main() {
    val garden = File("./src/main/resources/day12_input.txt").readLines().map { it.toList() }.toGrid()

    val regions = mutableListOf<MutableSet<Coordinate>>()

    garden.indices.forEach { (i, j) ->
        when {
            garden.getOrNull(i-1,j) == garden[i,j] && garden.getOrNull(i,j-1) == garden[i,j] -> {
                val setOne = regions.find { it.contains(i-1 to j) }!!
                val setTwo = regions.find { it.contains(i to j-1) }!!
                regions.remove(setOne)
                regions.remove(setTwo)
                regions.add((setOne + setTwo + (i to j)).toMutableSet())
            }
            garden.getOrNull(i-1,j) == garden[i,j] -> regions.find { it.contains(i-1 to j) }!!.add(i to j)
            garden.getOrNull(i,j-1) == garden[i,j] -> regions.find { it.contains(i to j-1) }!!.add(i to j)
            else -> regions.add(mutableSetOf(i to j))
        }
    }

    fun MutableSet<Coordinate>.calculatePerimeter() = sumOf { cell ->
        cell.getAdjacents().count { adj -> adj !in this }
    }

    fun MutableSet<Coordinate>.calculateCost() = size * calculatePerimeter()

    val answerPart1 = regions.sumOf { it.calculateCost() }
    println("Answer part 1: $answerPart1")

}

