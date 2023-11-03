package com.eronalves.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LastFirstPlayerDao {

    @Insert
    fun insert(lastPlayer: LastFirstPlayer)

    @Query("SELECT * FROM LastFirstPlayer WHERE ID = 1")
    fun checkIfHaveAFirstPlayer(): LastFirstPlayer?

    @Update
    fun updateLastFirstPlayer(lastPlayer: LastFirstPlayer)
}