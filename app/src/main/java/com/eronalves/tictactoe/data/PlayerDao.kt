package com.eronalves.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerDao {

    @Query("SELECT id from Player WHERE name=:name")
    fun findByName(name: String): Player?

    @Insert
    fun create(player: Player): Long

}