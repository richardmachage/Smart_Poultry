package com.example.smartpoultry

import junit.framework.TestCase.assertTrue
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun myFlagCell_ThresholdMet_ReturnsTrue() {
       val myTests = MyTests()

        // Action
        val result = myTests.myFlagCell(1)

        // Assertion
        assertTrue(result)

    }

}