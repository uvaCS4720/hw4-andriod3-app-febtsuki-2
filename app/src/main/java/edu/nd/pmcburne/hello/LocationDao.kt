package edu.nd.pmcburne.hello

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class LocationWithTag(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocations(locations: List<LocationEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTags(tags: List<LocationTagEntity>)

    @Query("""
        SELECT DISTINCT tag
        FROM location_tags
        ORDER BY tag ASC
    """)
    fun getAllTags(): Flow<List<String>>

    @Query("""
        SELECT l.id, l.name, l.description, l.latitude, l.longitude
        FROM locations l
        INNER JOIN location_tags t
        ON l.id = t.locationId
        WHERE t.tag = :selectedTag
        ORDER BY l.name ASC
    """)
    fun getLocationsForTag(selectedTag: String): Flow<List<LocationWithTag>>

    @Query("SELECT COUNT(*) FROM locations")
    suspend fun getLocationCount(): Int
}