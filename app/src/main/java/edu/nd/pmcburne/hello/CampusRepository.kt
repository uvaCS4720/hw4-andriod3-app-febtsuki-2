package edu.nd.pmcburne.hello

import edu.nd.pmcburne.hello.LocationDao
import edu.nd.pmcburne.hello.LocationEntity
import edu.nd.pmcburne.hello.LocationTagEntity
import edu.nd.pmcburne.hello.PlacemarkApi
import kotlinx.coroutines.flow.Flow

class CampusRepository(
    private val dao: LocationDao
) {
    fun getAllTags(): Flow<List<String>> = dao.getAllTags()

    fun getLocationsForTag(tag: String) = dao.getLocationsForTag(tag)

    suspend fun syncFromApi() {
        val placemarks = PlacemarkApi.retrofitService.getPlacemarks()

        val locations = placemarks.map {
            LocationEntity(
                id = it.id,
                name = it.name,
                description = it.description,
                latitude = it.visualCenter.latitude,
                longitude = it.visualCenter.longitude
            )
        }

        val tags = placemarks.flatMap { placemark ->
            placemark.tagList.map { tag ->
                LocationTagEntity(
                    locationId = placemark.id,
                    tag = tag
                )
            }
        }

        dao.insertLocations(locations)
        dao.insertTags(tags)
    }
}