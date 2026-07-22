package com.rahul.clearwalls.core.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PromptModerationTest {

    @Test
    fun `allows normal wallpaper prompts`() {
        listOf(
            "misty mountains at sunrise",
            "cyberpunk city with neon lights",
            "minimal gradient in pastel colors",
            "a red sports car on a coastal road",
            "abstract geometric pattern, dark theme",
            "essex countryside", // contains 'sex' as substring — must NOT match
            "sussex coastline at dusk",
            "middlesex village street in autumn"
        ).forEach { prompt ->
            assertTrue("should allow: $prompt", PromptModeration.isAllowed(prompt))
        }
    }

    @Test
    fun `blocks explicit prompts regardless of case`() {
        listOf(
            "nude portrait",
            "NSFW anime girl",
            "sexy woman on beach",
            "PORNOGRAPHIC scene",
            "hentai wallpaper",
            "gore and blood everywhere",
            "deepfake of a celebrity",
            "naked person"
        ).forEach { prompt ->
            assertFalse("should block: $prompt", PromptModeration.isAllowed(prompt))
        }
    }

    @Test
    fun `blocks terms embedded in longer prompts`() {
        assertFalse(PromptModeration.isAllowed("a beautiful landscape but make it nsfw please"))
        assertFalse(PromptModeration.isAllowed("photorealistic topless figure in renaissance style"))
    }
}
