package com.wolt.nearbyrestaurants.model

data class Restaurant(
    private val section: SectionItem
) {
    val id: String = section.venue!!.id

    val name: String = section.venue!!.name

    val shortDescription: String = section.venue!!.shortDescription

    val imageUrl: String = section.image.url
}
