package com.example.testincft.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.testincft.retrofit.GetCurrencyListAPI
import com.example.testincft.room.CurrencyRepository
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainFragmentViewModel : ViewModel() {

    private val currencyRepository = CurrencyRepository.get()
    val currencyListLiveData = currencyRepository.getCurrencies()

    fun requestData() {
        val baseUrl = "https://www.cbr-xml-daily.ru/"
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api: GetCurrencyListAPI = retrofit.create(GetCurrencyListAPI::class.java)
        api.getCurrencyList()?.enqueue(object : Callback<CurrencyDTO?> {
            override fun onResponse(call: Call<CurrencyDTO?>?, response: Response<CurrencyDTO?>) {
                val currenciesMap = response.body()?.Valute
                if (currenciesMap != null) {
                    currencyRepository.deleteAll()
                    for (valute in currenciesMap) {
                        try {
                            val currency = Currency(
                                valute.value?.ID!!,
                                valute.value?.NumCode!!,
                                valute.value?.CharCode!!,
                                valute.value?.Nominal!!,
                                valute.value?.Name!!,
                                valute.value?.Value!!,
                                valute.value?.Previous!!
                            )
                            currencyRepository.insertCurrency(currency)
                        } catch (e: Exception) {
                            Log.d("CurrencyList :: ", "Failure")
                        }
                    }


                }
            }

            override fun onFailure(call: Call<CurrencyDTO?>?, t: Throwable?) {
                Log.d("CurrencyList :: ", "Failure")
            }
        })
    }

}