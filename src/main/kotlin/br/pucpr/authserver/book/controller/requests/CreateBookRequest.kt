package br.pucpr.authserver.book.controller.requests

import br.pucpr.authserver.book.Book
import jakarta.validation.constraints.NotBlank

data class CreateBookRequest(
        @field:NotBlank
        val title: String,
        @field:NotBlank
        val author: String
) {
    fun toBook() = Book(
            title = title,
            author = author
    )
}
