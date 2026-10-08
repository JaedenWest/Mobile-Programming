package com.todolist.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.todolist.domain.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_items ORDER BY isCompleted ASC, dueDate ASC, id DESC")
    fun getAllTodoListItems(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo_items WHERE id = :id")
    suspend fun getItemById(id: Int): TodoEntity?

    @Query("SELECT * FROM todo_items WHERE id = :id")
    fun getItemByIdFlow(id: Int): Flow<TodoEntity?>

    @Query("SELECT * FROM todo_items WHERE isCompleted = 0 ORDER BY dueDate ASC, id DESC")
    fun getIncompleteTodoListItems(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(item: TodoEntity): Long

    @Update
    suspend fun updateTodoItem(item: TodoEntity)

    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteTodoItem(id: Int)

    @Query("DELETE FROM todo_items WHERE isCompleted = 1")
    suspend fun deleteCompletedItems()
}

@Database(entities = [TodoEntity::class], version = 2, exportSchema = false)
abstract class TodoListDatabase: RoomDatabase(){
    abstract fun todoListDao(): TodoListDao

    companion object {

        @Volatile
        private var INSTANCE: TodoListDatabase? = null

        fun getDatabase(context: Context): TodoListDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoListDatabase::class.java,
                    "todolist_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
