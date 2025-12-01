package aoc2024

import java.io.File

fun main() {
    val contents = File("./src/main/resources/aoc2024/day3_input.txt").readText()
    val regexPart1 = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)".toRegex()
    val answerPart1 = regexPart1.findAll(contents).map { it.destructured }.sumOf { (a, b) -> a.toInt() * b.toInt() }
    println("Answer part 1: $answerPart1")

    val regexPart2 = "mul\\(([0-9]{1,3}),([0-9]{1,3})\\)|don't\\(\\)|do\\(\\)".toRegex()
    val answerPart2 = regexPart2.findAll(contents).map { it.groupValues }.fold(0 to 1) { (total, switch), value ->
        when {
            value.first().contains("mul") -> (total + switch * value[1].toInt() * value[2].toInt()) to switch
            value.first() == "do()" -> total to 1
            value.first() == "don't()" -> total to 0
            else -> throw Exception("Unknown command ${value.first()}")
        }
    }.first
    println("Answer part 2: $answerPart2")
}

