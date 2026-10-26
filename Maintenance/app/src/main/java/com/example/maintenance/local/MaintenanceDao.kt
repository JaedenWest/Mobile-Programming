package com.example.maintenance.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.maintenance.MaintenanceEntity
import kotlinx.coroutines.flow.Flow

@Dao interface MaintenanceDao{
    @Query("SELECT * FROM maintenance_items ORDER BY type ASC, dueDate ASC, id DESC")
    fun getAllMaintenanceItems(): Flow<List<MaintenanceEntity>>
}