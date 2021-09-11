package com.example.testincft.ui.main

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(
    @PrimaryKey val id: String = "qqq",
    var numCode: String = "060",
    var charCode: String = "RUB",
    var nominal: Int = 1,
    var name: String = "Российский рубль",
    var value: Double = 1.0,
    var previous: Double = 0.0
)
