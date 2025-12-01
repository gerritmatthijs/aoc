package aoc2024

import java.io.File

fun main() {
    val (ordering, updates) = File("./src/main/resources/aoc2024/day5_input.txt").readLines().let { lines ->
        lines.subList(0, lines.indexOf("")).map { it.split('|').map(String::toInt) } to
                lines.subList(lines.indexOf("") + 1, lines.size).map { it.split(',').map(String::toInt) }
    }
    val orderGraph = ordering.map { it.first() }.toSet().associateWith { element ->
        ordering.filter { it.first() == element }.map { it.last() }
    }

    fun List<Int>.isOrdered() = indices.all { i ->
        (0..<i).all { k ->
            orderGraph[this[i]]?.let {
                this[k] !in it
            } ?: true
        }
    }

    val answerPart1 = updates.filter { update -> update.isOrdered() }
        .sumOf { update -> update[(update.size - 1)/2] }
    println("Answer part 1: $answerPart1")

    class Ordering: Comparator<Int> {
        override fun compare(o1: Int, o2: Int): Int {
            orderGraph[o1]?.let { if (o2 in it) return 1 }
            orderGraph[o2]?.let { if (o1 in it) return -1 }
            return 0
        }
    }

    val answerPart2 = updates.filterNot { it.isOrdered() }
        .sumOf { update -> update.sortedWith(Ordering())[(update.size - 1)/2] }
    println("Answer part 2: $answerPart2")
}

