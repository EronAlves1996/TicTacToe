package com.eronalves.tictactoe.data

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimetamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun winnerToNumber(winner: Winner): Int? {
        return winner.ordinal
    }

    @TypeConverter
    fun numberToWinner(number: Int): Winner {
        return enumValues<Winner>()[number]
    }
}