package br.pucpr.authserver.category.controller.responses

import br.pucpr.authserver.book.Book

data class CategoryWithBooksResponse(
        val id: Long,
        val name: String,
        val books: MutableSet<Book>
)
