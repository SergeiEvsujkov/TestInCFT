package com.example.testincft.ui.main

data class CurrencyDTO (
    val Date: String?,
    val PreviousDate: String?,
    val PreviousURL: String?,
    val Timestamp: String?,
    val Valute: Map<String?,CurDTO?>,

)
data class CurDTO(
    val ID: String?,
    val NumCode: String?,
    val CharCode: String?,
    val Nominal: Int?,
    val Name: String?,
    val Value: Double?,
    val Previous: Double?,
)

