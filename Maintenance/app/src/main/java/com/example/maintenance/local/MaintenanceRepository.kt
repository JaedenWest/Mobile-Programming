package com.example.maintenance.local

import android.util.Log
import com.maintenance.domain.MaintenanceEntity
import kotlinx.coroutines.flow.Flow

class MaintenanceRepository(private val maintenanceDao: MaintenanceDao){
    //emits the full list of items from the database
    val MaintenanceList: Flow<List<MaintenanceEntity>> = maintenanceDao.getAllMaintenanceListItems()

    //fetches a task by a ID
    suspend fun getItemById(id: Int): MaintenanceEntity? = maintenanceDao.getItemsById(id)

    //inserts a new task into the database
    suspend fun insertMaintenanceItem(item:MaintenanceEntity){
        maintenanceDao.insertMaintenanceItem(item)
        Log.d("Repository", "ItemInsertd: ${item.title}")
    }

    //updates an existing task item in the database
    suspend fun updateMaintenanceItem(item:MaintenanceEntity){
        maintenanceDao.updateMaintenanceItem(item)
    }

    //deletes a task
    suspend fun deleteMaintenanceItem(item:MaintenanceEntity){
        maintenanceDao.deleteMaintenanceItem(item)
    }
}