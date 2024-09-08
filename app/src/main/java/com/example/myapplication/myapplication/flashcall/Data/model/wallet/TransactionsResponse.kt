package com.example.myapplication.myapplication.flashcall.Data.model.wallet

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TransactionsResponse(
    val transactions: List<Transaction>?=null
)
data class Transaction(
    val __v: Int,
    val _id: String,
    val amount: Double,
    val createdAt: String,
    val type: String,
    val updatedAt: String,
    val userId: String,
    val userType: String
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