package com.wolt.nearbyrestaurants.model

import com.google.gson.annotations.SerializedName

data class Section(
    val items: List<SectionItem>
)

data class SectionItem(
    val venue: Venue?,
    val image: SectionImage
)

data class Venue(
    val id: String,
    val name: String,
    @SerializedName("short_description") val shortDescription: String
)

data class SectionImage(
    val url: String
)
