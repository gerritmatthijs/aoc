package org.example

import java.io.File

fun main() {
    val contents = File("./src/main/resources/day4_input.txt").readLines().map { it.toCharArray().toList() }

    fun checkPosition(row: Int, col: Int): Boolean =
        contents[row][col] == 'A' && listOf(contents[row-1][col-1], contents[row-1][col+1], contents[row+1][col-1], contents[row+1][col+1]).let { chars ->
            chars.count { it == 'M' } == 2 && chars.count { it == 'S' } == 2 && chars[1] != chars[2]
        }

    val dSize = contents.size - 1
    val horizontal = contents
    val vertical = contents.indices.map { row ->
        contents.first().indices.map { col -> contents[col][row] }
    }
    val mainDiag = (-dSize..dSize).map { diag ->
        contents.indices.mapNotNull { row -> contents[row].getOrNull(row + diag) }
    }
    val offDiag = (0..dSize * 2).map { diag ->
        contents.indices.mapNotNull { row -> contents[row].getOrNull(diag - row) }
    }
    val answerPart1 = listOf(horizontal, vertical, mainDiag, offDiag).flatMap { m -> listOf(m, m.map { it.reversed() }) }
        .sumOf { Regex("XMAS").findAll(it.concatToString()).count() }

    println(answerPart1)

    val answerPart2 = (1..(contents.size-2)).sumOf { row ->
        (1..(contents.size-2)).count { col -> checkPosition(row, col) }
    }
    println(answerPart2)
}

fun List<List<Char>>.concatToString(): String = joinToString("\n") { it.joinToString("") }


