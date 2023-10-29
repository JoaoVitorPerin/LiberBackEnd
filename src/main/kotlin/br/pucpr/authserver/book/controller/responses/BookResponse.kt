package br.pucpr.authserver.book.controller.responses

import br.pucpr.authserver.book.Book

data class BookResponse(
        val id: Long,
        val title: String,
        val author: String
) {
    constructor(book: Book) : this(book.id!!, book.title, book.author)
}
