package com.example.maintenance

import android.content.Context
import androidx.room.Room
import com.example.maintenance.data.local.MaintenanceDao
import com.example.maintenance.data.local.MaintenanceDatabase
import com.example.maintenance.data.local.MaintenanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MaintenanceTask{

    //provides a singleton instance of the room database
    @Provides
    @Singleton
    fun provideMaintenanceDatabase(
        @ApplicationContext context: Context
    ): MaintenanceDatabase{
        return Room.databaseBuilder(
            context,
            MaintenanceDatabase::class.java,
            "maintenance_database"
        )
            .build()
    }

    //provides the MaintenanceDao instance from the database
    @Provides
    fun provideMaintenanceDao(database: MaintenanceDatabase): MaintenanceDao{
        return database.maintenanceDao()
    }

    //provides the Singleton MaintenanceRepository wrapping the DAO

    @Provides
    @Singleton
    fun provideMaintenanceRepository(maintenanceDao: MaintenanceDao): MaintenanceRepository {
        return MaintenanceRepository(maintenanceDao)
    }
}