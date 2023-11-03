package com.eronalves.tictactoe.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime

@Entity
data class Round(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "player_1_id") val player1Id: Int?,
    @ColumnInfo(name = "player_2_id") val player2Id: Int?,
    @ColumnInfo(name = "winner") val winner: Int?,
    @ColumnInfo(name = "completed_at") val completedAt: LocalDateTime?
)

data class RoundAndPlayers(
    @Embedded val round: Round,
    @Relation(parentColumn = "id", entityColumn = "player_1_id") val player1: Player?,
    @Relation(parentColumn = "id", entityColumn = "player_2_id") val player2: Player?
)
