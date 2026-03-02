package com.rahul.clearwalls.domain.model

enum class ImageQuality(val displayName: String, val label: String, val isPremium: Boolean) {
    LOW("Low", "480p", false),
    HD("HD", "1080p", false),
    TWO_K("2K", "1440p", false),
    FOUR_K("4K", "2160p", true);
}
