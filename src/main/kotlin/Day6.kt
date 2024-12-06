package org.example

import java.io.File

fun main() {
    val grid = File("./src/main/resources/day6_input.txt").readLines().map { it.toList() }

    fun List<List<Char>>.print() = onEach { println(it) }

    fun List<List<Char>>.rotateLeft() = this.indices.map { i ->
        this[i].indices.map { j -> this[j][grid.size-1-i] }
    }

    fun List<List<Char>>.moveLeft(row: Int, beginCol: Int, endCol: Int) = toMutableList().also {
        it[row] = it[row].subList(0, endCol) + listOf(if (endCol == 0) 'X' else '^') + "X".repeat(beginCol - endCol).toList() + it[row].subList(beginCol + 1, grid.size)
    }

    fun List<List<Char>>.findGuard() = indexOfFirst { it.contains('^') }.takeIf { it >= 0 }?.let { row ->
        row to this[row].indexOf('^')
    }

    fun List<List<Char>>.play(): List<List<Char>> = findGuard()?.let { (row, beginCol) ->
        val endCol = this[row].subList(0, beginCol).indexOfLast { it == '#' } + 1
        moveLeft(row, beginCol, endCol).rotateLeft().play()
    } ?: this

    val answerPart1 = grid.rotateLeft().play().print().sumOf { row -> row.count { it == 'X' } }
    println("Answer part 1: $answerPart1")
}

