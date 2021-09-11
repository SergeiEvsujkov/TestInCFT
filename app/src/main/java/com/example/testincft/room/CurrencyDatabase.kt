package com.example.testincft.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.testincft.ui.main.Currency

@Database(entities = [Currency::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDAO
}