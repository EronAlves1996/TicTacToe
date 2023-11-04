package com.eronalves.tictactoe.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.time.LocalDateTime
import java.util.Date

enum class Winner {
    Player1, Player2, Match
}

@Entity
data class Round(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "table_size") val tableSize: Int?,
    @ColumnInfo(name = "player_1_id") val player1Id: Long?,
    @ColumnInfo(name = "player_2_id") val player2Id: Long?,
    @ColumnInfo(name = "winner") val winner: Winner?,
    @ColumnInfo(name = "started_at") val startedAt: Date?
)

data class RoundAndPlayers(
    @Embedded val round: Round,
    @Relation(parentColumn = "player_1_id", entityColumn = "id") val player1: Player?,
    @Relation(parentColumn = "player_2_id", entityColumn = "id") val player2: Player?
)
