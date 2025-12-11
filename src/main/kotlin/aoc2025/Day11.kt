package aoc2025

import java.io.File


fun main() {
    val nodes = File("./src/main/resources/aoc2025/day11_input.txt").readLines().associate { line ->
        line.split(":").let { (node, outputNodes) ->
            node to outputNodes.trim().split(" ")
        }
    }

    val paths = generateSequence(listOf("you")) { currentPaths ->
        currentPaths.flatMap { node -> if (node == "out") listOf(node) else (nodes[node] ?: emptyList()) }
            .takeIf { paths -> paths != currentPaths }
    }.last()
    println("Answer part 1: ${paths.count()}")
}
