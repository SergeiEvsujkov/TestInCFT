package com.example.testincft

import android.app.Application
import com.example.testincft.room.CurrencyRepository

class TestInCFTApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CurrencyRepository.initialize(this)
    }
}