package edu.nd.pmcburne.hello

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlacemarkDto(
    val id: Int,
    val name: String,
    val description: String,
    @SerialName("tag_list")
    val tagList: List<String>,
    @SerialName("visual_center")
    val visualCenter: VisualCenterDto
)

@Serializable
data class VisualCenterDto(
    val latitude: Double,
    val longitude: Double
)