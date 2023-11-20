package br.pucpr.authserver.category.controller.requests

import br.pucpr.authserver.book.Book
import br.pucpr.authserver.category.Category
import jakarta.validation.constraints.NotBlank

data class CategoryRequest(
        @field:NotBlank(message = "Category name is required")
        val name: String
){
        fun toCategory() = Category(
                name = name
        )
}
