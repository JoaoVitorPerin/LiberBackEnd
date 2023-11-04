package br.pucpr.authserver.category.controller.requests

import jakarta.validation.constraints.NotBlank

data class CategoryRequest(
        @field:NotBlank(message = "Category name is required")
        val name: String
)
