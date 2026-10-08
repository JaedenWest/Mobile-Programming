package com.todolist

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.todolist.data.local.TodoListDao
import com.todolist.data.local.TodoListDatabase
import com.todolist.data.local.TodoListRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoListDatabase(
        @ApplicationContext context: Context
    ): TodoListDatabase {
        return Room.databaseBuilder(
            context,
            TodoListDatabase::class.java,
            "todolist_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideTodoListDao(database: TodoListDatabase): TodoListDao {
        return database.todoListDao()
    }

    @Provides
    @Singleton
    fun provideTodoListRepository(todoListDao: TodoListDao): TodoListRepository {
        return TodoListRepository(todoListDao)
    }

}
