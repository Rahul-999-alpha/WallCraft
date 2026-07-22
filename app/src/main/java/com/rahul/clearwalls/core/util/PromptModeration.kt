package com.rahul.clearwalls.core.util

/**
 * Deterministic client-side prompt gate for AI generation.
 *
 * First line of defence required by Play's AI-Generated Content policy; the
 * Pollinations request additionally passes safe=true so the server-side NSFW
 * filter backstops anything this list misses. Deliberately over-blocks: a false
 * positive costs one retry, a false negative can cost the Play listing.
 */
object PromptModeration {

    private val BLOCKED_PATTERN = Regex(
        listOf(
            // Sexual / explicit
            "nude", "nudes", "nudity", "naked", "nsfw", "porn\\w*", "erotic\\w*",
            "sex", "sexual", "sexy", "hentai", "xxx", "fetish", "lingerie",
            "topless", "undress\\w*", "strip\\w*", "genital\\w*", "breasts?",
            "boobs?", "nipples?",
            // Minors in any sexualised context are already covered by the terms
            // above; block common evasion phrasings outright as well.
            "loli\\w*", "shota\\w*", "child\\s*(bride|model)",
            // Graphic violence / gore
            "gore", "gory", "behead\\w*", "dismember\\w*", "mutilat\\w*",
            "corpse", "torture\\w*",
            // Real-person abuse
            "deepfake\\w*",
            // Hate symbols
            "swastika", "nazi\\w*"
        ).joinToString(separator = "|", prefix = "\\b(", postfix = ")\\b"),
        RegexOption.IGNORE_CASE
    )

    fun isAllowed(prompt: String): Boolean = !BLOCKED_PATTERN.containsMatchIn(prompt)

    const val BLOCKED_MESSAGE =
        "That prompt isn't allowed. Try scenery, art styles, patterns, or abstract themes."
}
