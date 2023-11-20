package br.pucpr.authserver.book.controller.requests

import br.pucpr.authserver.book.Book
import br.pucpr.authserver.category.Category
import br.pucpr.authserver.category.controller.requests.CategoryRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

data class CreateBookRequest(
        @field:NotBlank(message = "Title is required")
        val title: String,

        @field:NotBlank(message = "Author is required")
        val author: String,

        @field:Valid
        val categories: MutableSet<CategoryRequest>
) {
    fun toBook() = Book(
            title = title,
            author = author,
            categories = categories.map { Category(name = it.name) }.toMutableSet()
    )
}