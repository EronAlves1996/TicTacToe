package com.eronalves.tictactoe.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class Move(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "t_row") val row: Int?,
    @ColumnInfo(name = "t_column") val column: Int?,
    @ColumnInfo(name = "player_id") val playerId: Int?,
    @ColumnInfo(name = "round_id") val roundId: Int?
)

data class MovePlayerAndRound(
    @Embedded val move: Move,
    @Relation(
        parentColumn = "id",
        entityColumn = "player_id"
    ) val player: Player,
    @Relation(
        parentColumn = "id",
        entityColumn = "round_id",
    ) val round: Round
)