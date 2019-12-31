package com.whizsid.subtitleadjust.libTest

import com.whizsid.subtitleadjust.lib.SpeedCalculator
import com.whizsid.subtitleadjust.lib.Subtitle
import com.whizsid.subtitleadjust.lib.SubtitleAdjust
import com.whizsid.subtitleadjust.lib.Time
import org.junit.Test
import org.junit.Assert.*

// Offset = 1
// Speed = 2
// Error Time   Actual Time
//      0           0
//      5           7
//      7           11
//      9           15
//      11          19
//      13          23
//      15          27
class SpeedCalculatorTest {

    private val adjustList = mutableListOf(
        SubtitleAdjust(Subtitle(0,1,Time(5),Time(7),"subtitle1"),Time(7)),
        SubtitleAdjust(Subtitle(1,2,Time(9),Time(11),"subtitle2"),Time(15)),
        SubtitleAdjust(Subtitle(4,5,Time(11),Time(13),"subtitle4"),Time(19))
    )

    @Test
    fun testCalculate(){
        val calculator = SpeedCalculator(adjustList)

        calculator.calculate()

        assertEquals(calculator.offset,3)
        assertEquals(calculator.speed,2)
    }

    @Test
    fun testCorrect(){
        val calculator = SpeedCalculator(adjustList)

        calculator.calculate()

        val corrected = calculator.correct(Subtitle(2,3,Time(13),Time(15),"test correct"))

        assertEquals(corrected.getStartTime().toInt(),23)
        assertEquals(corrected.getEndTime().toInt(),27)
    }
}