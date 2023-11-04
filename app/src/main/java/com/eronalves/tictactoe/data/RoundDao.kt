package com.eronalves.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface RoundDao {
    @Insert
    fun create(round: Round): Long?

}