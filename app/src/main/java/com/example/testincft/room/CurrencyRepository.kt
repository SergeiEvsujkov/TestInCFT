package com.example.testincft.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.testincft.ui.main.Currency
import java.util.concurrent.Executors


private const val DATABASE_NAME = "currency-database"

class CurrencyRepository private constructor(context: Context) {

    private val database: CurrencyDatabase = Room.databaseBuilder(
        context.applicationContext,
        CurrencyDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val currencyDao = database.currencyDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getCurrencies(): LiveData<List<Currency>> = currencyDao.getCurrencies()
    fun getCurrency(id: String): LiveData<Currency?> = currencyDao.getCurrency(id)
    fun insertCurrency(currency: Currency) {
        executor.execute {
            currencyDao.insertCurrency(currency)
        }
    }

    fun deleteAll() {
        executor.execute {
            currencyDao.deleteAll()
        }
    }

    companion object {
        private var INSTANCE: CurrencyRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CurrencyRepository(context)
            }
        }

        fun get(): CurrencyRepository {
            return INSTANCE ?: throw IllegalStateException("CurrencyRepository must be initialized")
        }
    }
}
