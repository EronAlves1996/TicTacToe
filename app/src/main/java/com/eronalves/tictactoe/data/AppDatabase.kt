package com.eronalves.tictactoe.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Move::class, Player::class, Round::class, LastFirstPlayer::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun moveDao(): MoveDao
    abstract fun roundDao(): RoundDao
    abstract fun lastFirstPlayerDao(): LastFirstPlayerDao
}