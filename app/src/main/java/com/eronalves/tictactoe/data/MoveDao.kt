package com.eronalves.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface MoveDao {

    @Insert
    fun insertAll(vararg moves: Move)
}