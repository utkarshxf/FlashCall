package com.example.myapplication.myapplication.flashcall.Data.model.wallet

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TransactionResponse(
    val transactions: List<Transaction>? = null
)

data class Transaction(

    val _id : String? =null,
    val userId: String? =null,
    val userTypeId: String? =null,
    val amount: Double? =null,
    val type: String? =null,
    val createdAt: String? =null,
    val updatedAt: String? =null,
    val __v: Int? =null
)

data class TransactionGroup(

    val date : LocalDate? = null,
    val transactions : List<Transaction>? = null

)

fun List<Transaction>.groupByDate(): List<TransactionGroup> {
    return groupBy {
    LocalDateTime.parse(it.createdAt, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
}.map { (date, transactions) -> TransactionGroup(date, transactions) }
}