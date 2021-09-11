package com.example.testincft.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.testincft.ui.main.Currency

@Dao
interface CurrencyDAO {
    @Query("SELECT * FROM currency")
    fun getCurrencies(): LiveData<List<Currency>>

    @Query("SELECT * FROM currency WHERE id=(:id)")
    fun getCurrency(id: String): LiveData<Currency?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCurrency(currency: Currency)

    @Query("DELETE FROM currency")
    fun deleteAll()
}