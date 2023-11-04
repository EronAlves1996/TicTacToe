package com.eronalves.tictactoe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LastFirstPlayer(@PrimaryKey val id: Long, @ColumnInfo("p_name") val name: String?)