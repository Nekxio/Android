package fr.iutlens.hilaireghesquiere.minews

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface articleDao {
    @Query("SELECT * FROM article ORDER BY pubDate DESC")
    fun getAll(): Flow<List<Article>>

    @Query("SELECT * FROM article WHERE guid LIKE :guid LIMIT 1")
    suspend fun findByGUID(guid: String): Article

    @Query("DELETE FROM article")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg elements: Article)

    @Delete
    suspend fun delete(element: Article)
}