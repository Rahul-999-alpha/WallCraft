package com.rahul.clearwalls.core.util

import org.junit.Assert.assertEquals
import org.junit.Test

class AdTuningTest {

    @Test
    fun `unset key (0) falls back to default`() {
        assertEquals(8L, AdTuning.resolve(raw = 0L, default = 8L, floor = 4L))
    }

    @Test
    fun `negative value falls back to default`() {
        assertEquals(8L, AdTuning.resolve(raw = -5L, default = 8L, floor = 4L))
    }

    @Test
    fun `console override is used when above floor`() {
        assertEquals(6L, AdTuning.resolve(raw = 6L, default = 8L, floor = 4L))
    }

    @Test
    fun `console value below floor is clamped to floor`() {
        assertEquals(4L, AdTuning.resolve(raw = 1L, default = 8L, floor = 4L))
        assertEquals(60L, AdTuning.resolve(raw = 5L, default = 180L, floor = 60L))
    }
}
