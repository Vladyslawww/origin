package com.holikov.data.listing

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.*
import com.holikov.data.listing.model.db.GoodsItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GoodsDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(good: GoodsItemEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(good: GoodsItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(good: List<GoodsItemEntity>)

    @Query("SELECT * FROM goods WHERE listing_id = :id")
    suspend fun getItem(id: Long): List<GoodsItemEntity>

    @Query("DELETE FROM goods WHERE listing_id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM goods WHERE description LIKE :query OR title LIKE :query OR currency_code LIKE :query OR price LIKE :query ORDER BY timestamp ASC")
    fun search(query: String): Flow<List<GoodsItemEntity>>

    @Query("SELECT * FROM goods ORDER BY timestamp ASC")
    fun getAll(): Flow<List<GoodsItemEntity>>

    @Query("DELETE FROM goods")
    suspend fun deleteAll()
}