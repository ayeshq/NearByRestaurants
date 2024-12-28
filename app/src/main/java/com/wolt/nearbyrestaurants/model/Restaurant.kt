package com.wolt.nearbyrestaurants.model

data class Restaurant(
    val id: String,
    val name: String,
    val shortDescription: String,
    val imageUrl: String,
    val isSaved: Boolean = false
) {
    constructor(
        section: SectionItem,
        isSaved: Boolean = false
    ) : this(
        id = section.venue!!.id,
        name = section.venue.name,
        shortDescription = section.venue.shortDescription,
        imageUrl = section.image.url,
        isSaved = isSaved
    )
}
