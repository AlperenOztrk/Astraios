package com.wiuma.astraios.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wiuma.astraios.models.User

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(user: User): Long

    @Query("SELECT * from user WHERE Id = :userID")
    suspend fun getUser(userID: String?): User

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Update
    suspend fun updateUser(user: User)
}