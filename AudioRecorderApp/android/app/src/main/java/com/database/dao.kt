package com.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {
    @Insert
    suspend fun insertAudio(dbAudio: DbAudio)

    @Delete
    suspend fun deleteAudio(dbAudio: DbAudio)

    @Query("SELECT * FROM db_audio ORDER BY createdAt DESC")
    suspend fun getAllAudio(): List<DbAudio>

    @Query("DELETE FROM db_audio")
    suspend fun deleteAllAudio()
}