package com.practice.bookstore.books

import org.springframework.stereotype.Service

@Service
class BookService(val bookRepository: BookRepository) {

    fun listAllBooks(): Collection<Book>? {
        return bookRepository.findAll()
    }

    fun createBook(book: Book): Book {
        return bookRepository.save(book)
    }






}
