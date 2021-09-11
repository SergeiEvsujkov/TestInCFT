package com.example.testincft.retrofit

import com.example.testincft.ui.main.CurrencyDTO
import retrofit2.Call
import retrofit2.http.GET

interface GetCurrencyListAPI {
    @GET("daily_json.js")
    fun getCurrencyList(): Call<CurrencyDTO?>?
}