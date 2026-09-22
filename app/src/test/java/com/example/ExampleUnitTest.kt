package com.example

import com.example.logic.GameEngine
import com.example.model.ItemColor
import com.example.model.Tube
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testLevelCreation() {
    val tubes = GameEngine.createTubesForLevel(1)
    assertEquals(3, tubes.size)
    assertFalse(tubes[0].isEmpty)
    assertFalse(tubes[1].isEmpty)
    assertTrue(tubes[2].isEmpty)
  }

  @Test
  fun testMoveValidation() {
    val src = Tube(id = 0, items = listOf(ItemColor.RED, ItemColor.BLUE))
    val emptyDst = Tube(id = 1, items = emptyList())
    val matchingDst = Tube(id = 2, items = listOf(ItemColor.GREEN, ItemColor.BLUE))
    val mismatchDst = Tube(id = 3, items = listOf(ItemColor.RED))
    val fullDst = Tube(id = 4, items = listOf(ItemColor.BLUE, ItemColor.BLUE, ItemColor.BLUE, ItemColor.BLUE))

    // Can move to empty tube
    assertTrue(GameEngine.canMove(src, emptyDst))
    // Can move onto matching top color
    assertTrue(GameEngine.canMove(src, matchingDst))
    // Cannot move onto mismatching top color
    assertFalse(GameEngine.canMove(src, mismatchDst))
    // Cannot move onto full tube
    assertFalse(GameEngine.canMove(src, fullDst))
    // Cannot move from tube to itself
    assertFalse(GameEngine.canMove(src, src))
  }

  @Test
  fun testLevelSolvedCondition() {
    val solvedTubes = listOf(
      Tube(id = 0, items = listOf(ItemColor.RED, ItemColor.RED, ItemColor.RED, ItemColor.RED)),
      Tube(id = 1, items = listOf(ItemColor.BLUE, ItemColor.BLUE, ItemColor.BLUE, ItemColor.BLUE)),
      Tube(id = 2, items = emptyList())
    )
    assertTrue(GameEngine.isLevelSolved(solvedTubes))

    val unsolvedTubes = listOf(
      Tube(id = 0, items = listOf(ItemColor.RED, ItemColor.BLUE, ItemColor.RED, ItemColor.RED)),
      Tube(id = 1, items = listOf(ItemColor.BLUE, ItemColor.BLUE, ItemColor.BLUE, ItemColor.RED)),
      Tube(id = 2, items = emptyList())
    )
    assertFalse(GameEngine.isLevelSolved(unsolvedTubes))
  }

  @Test
  fun testFindHint() {
    val tubes = GameEngine.createTubesForLevel(1)
    val hint = GameEngine.findHint(tubes)
    assertNotNull(hint)
    val (from, to) = hint!!
    assertTrue(GameEngine.canMove(tubes[from], tubes[to]))
  }
}
