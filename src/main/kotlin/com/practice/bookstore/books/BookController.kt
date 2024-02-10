package com.practice.bookstore.books

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.net.http.HttpHeaders

@RestController
@RequestMapping("/books")
class BookController(val bookService: BookService) {

    @GetMapping
    fun listAllBooks(): Collection<Book>? = bookService.listAllBooks();

    @PostMapping
    fun createBook(book: Book): Book = bookService.createBook(book);

}