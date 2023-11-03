package com.eronalves.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface PlayerDao {
    @Insert
    fun insertAll(vararg players: Player)
}