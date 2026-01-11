package com.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_audio")
data class DbAudio(
    val fileName: String,
    val filePath: String,
    val duration: Long, // in milliseconds
    val createdAt: Long // timestamp
){
    @PrimaryKey(autoGenerate = true) var id = 0
}