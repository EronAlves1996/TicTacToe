package com.eronalves.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MoveDao {


    @Insert
    fun insertAll(vararg moves: Move)

}