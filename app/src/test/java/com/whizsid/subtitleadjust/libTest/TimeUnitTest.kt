package com.whizsid.subtitleadjust.libTest

import com.whizsid.subtitleadjust.lib.Time
import org.junit.Test
import org.junit.Assert.*

class TimeUnitTest {
    @Test
    fun stringConstructorTest(){

        val middle = "02:04:05,400"
        val zero = "00:00:00,000"
        val max = "23:59:59,999"

        val middleTime = Time(middle)
        assertEquals(middleTime.getHour(),2)
        assertEquals(middleTime.getMinute(), 4)
        assertEquals(middleTime.getSecond(),5)
        assertEquals(middleTime.getMilli(),400)

        val zeroTime = Time(zero)
        assertEquals(zeroTime.getHour(),0)
        assertEquals(zeroTime.getMinute(), 0)
        assertEquals(zeroTime.getSecond(),0)
        assertEquals(zeroTime.getMilli(),0)

        val maxTime = Time(max)
        assertEquals(maxTime.getHour(),23)
        assertEquals(maxTime.getMinute(), 59)
        assertEquals(maxTime.getSecond(),59)
        assertEquals(maxTime.getMilli(),999)
    }

    @Test
    fun intConstructorTest(){
        val middle = 400+(5*1000)+(4*60*1000)+(2*3600*1000)
        val zero = 0
        val max = 999+(59*1000)+(59*60*1000)+(23*3600*1000)

        val middleTime = Time(middle.toLong())
        assertEquals(middleTime.getHour(),2)
        assertEquals(middleTime.getMinute(), 4)
        assertEquals(middleTime.getSecond(),5)
        assertEquals(middleTime.getMilli(),400)

        val zeroTime = Time(zero.toLong())
        assertEquals(zeroTime.getHour(),0)
        assertEquals(zeroTime.getMinute(), 0)
        assertEquals(zeroTime.getSecond(),0)
        assertEquals(zeroTime.getMilli(),0)

        val maxTime = Time(max.toLong())
        assertEquals(maxTime.getHour(),23)
        assertEquals(maxTime.getMinute(), 59)
        assertEquals(maxTime.getSecond(),59)
        assertEquals(maxTime.getMilli(),999)
    }

    @Test
    fun toIntTest(){
        val tests = arrayListOf("02:04:05,400","00:00:00,000","23:59:59,999")
        val results = arrayListOf(400+(5*1000)+(4*60*1000)+(2*3600*1000),0,999+(59*1000)+(59*60*1000)+(23*3600*1000))

        var i =0
        tests.forEach {
            val time = Time(it)

            assertEquals(time.toInt(),results[i])
            i++
        }
    }

    @Test
    fun validateStringTest(){
        val tests = arrayListOf("","wrong","02:04:05,400","02:0a:05,400","02:04:05,40+0","02:04:05.500","00:00:00,000")
        val asserts = arrayListOf(false,false,true,false,true,false,true)

        var i =0

        tests.forEach {
            assertEquals(Time.validateString(it),asserts[i])
            i++
        }
    }
}