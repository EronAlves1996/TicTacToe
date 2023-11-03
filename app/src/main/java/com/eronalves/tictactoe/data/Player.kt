package com.eronalves.tictactoe.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(@PrimaryKey val id: Int, @ColumnInfo(name = "name") val name: String?)