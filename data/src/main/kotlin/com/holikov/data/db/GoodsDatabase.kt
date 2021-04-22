package com.holikov.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.holikov.data.listing.GoodsDao
import com.holikov.data.listing.model.db.GoodsItemEntity


@Database(
    entities = [GoodsItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GoodsDatabase : RoomDatabase() {

    abstract fun goodsDao(): GoodsDao

    companion object {
        fun create(context: Context): GoodsDatabase =
            Room.databaseBuilder(context, GoodsDatabase::class.java, "goods").build()
    }
}