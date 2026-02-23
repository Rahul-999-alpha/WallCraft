package com.rahul.wallcraft.data.remote.dto

import com.google.gson.annotations.SerializedName

data class StabilityAiRequest(
    val prompt: String,
    @SerializedName("output_format") val outputFormat: String = "jpeg",
    @SerializedName("aspect_ratio") val aspectRatio: String = "9:16"
)

data class StabilityAiResponse(
    val image: String,
    @SerializedName("finish_reason") val finishReason: String,
    val seed: Long
)
