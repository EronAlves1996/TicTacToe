package com.eronalves.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface RoundDao {
    @Insert
    fun create(round: Round): Long?

    @Transaction
    @Query("SELECT * FROM Round ORDER BY started_at DESC LIMIT 6 OFFSET :start")
    fun getPaginatedFrom(start: Int): List<RoundAndPlayers>

    @Query("SELECT COUNT(id) FROM Round")
    fun getItemCount(): Int
}