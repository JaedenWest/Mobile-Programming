package com.todolist.data.local

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BroadcastReceiverEntryPoint {
    fun getMyRepository(): TodoListRepository
}