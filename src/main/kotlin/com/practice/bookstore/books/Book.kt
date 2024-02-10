package com.practice.bookstore.books

import org.jooq.Record

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val pages: Int,
    val yearString: String
) {
    companion object {
        fun fromRow(record: Record): Book {
            return Book(
                    record.get("id").toString().toLong(),
                    record.get("title").toString(),
                    record.get("author").toString(),
                    record.get("pages").toString().toInt(),
                    record.get("year_string").toString()
            )
        }
    }
}
