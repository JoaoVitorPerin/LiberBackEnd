package br.pucpr.authserver.category.controller.requests

import br.pucpr.authserver.category.Category
import jakarta.validation.constraints.NotBlank

data class CreateCategoryRequest(
        @field:NotBlank
        val name: String
) {
    fun toCategory() = Category(
            name = name
    )
}

