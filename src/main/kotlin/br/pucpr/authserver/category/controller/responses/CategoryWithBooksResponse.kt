package br.pucpr.authserver.category.controller.responses

import BookResponse
import br.pucpr.authserver.book.Book

data class CategoryWithBooksResponse(
        val id: Long,
        val name: String,
        val books: MutableSet<BookResponse>
)

