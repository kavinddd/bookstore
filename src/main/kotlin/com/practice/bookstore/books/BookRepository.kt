package com.practice.bookstore.books

import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class BookRepository(val dslContext: DSLContext) {
    val SCHEMA_NAME: String = "bookstore"

    fun findAll(): Collection<Book>? {
        val records = dslContext.select()
                .from( DSL.table("bookstore.book"))
                .fetch();

        return records.map { Book.fromRow(it) };
    }

    fun save(book: Book): Book {

        dslContext.insertInto(DSL.table("$SCHEMA_NAME.app_user"),
                DSL.field("title"),
                DSL.field("author"),
                DSL.field("pages"),
                DSL.field("year_string"),
                )
                .values(
                        book.title,
                        book.author,
                        book.pages,
                        book.yearString
                )
                .execute()

        return book
    }
}
