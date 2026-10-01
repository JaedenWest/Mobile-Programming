package com.todolist.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.todolist.domain.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todo_items ORDER BY id ASC")
    fun getAllTodoListItems(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo_items WHERE id = :id")
    suspend fun getItemById(id: Int): TodoEntity?

    @Query("SELECT * FROM todo_items WHERE isCompleted = false ORDER BY id ASC")
    fun getIncompleteTodoListItems(): Flow<List<TodoEntity>>

    @Insert
    suspend fun insertTodoItem(item: TodoEntity)

    @Update
    suspend fun updateTodoItem(item: TodoEntity)

    @Query("DELETE FROM todo_items WHERE id = :id")
    suspend fun deleteTodoItem(id: Int)
}

@Database(entities = [TodoEntity::class], version = 1, exportSchema = false)
abstract class TodoListDatabase: RoomDatabase(){
    abstract fun todoListDao(): TodoListDao

    companion object{

        @Volatile
        private var INSTANCE: TodoListDatabase? = null

        fun getDatabase(context: Context): TodoListDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoListDatabase::class.java,
                    "todolist_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
