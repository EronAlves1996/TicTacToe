package com.eronalves.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface RoundDao {
    @Insert
    fun insertAll(vararg rounds: Round)

}