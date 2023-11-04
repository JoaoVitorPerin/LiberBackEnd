package br.pucpr.authserver.category.controller.requests

import jakarta.validation.constraints.NotBlank

data class PatchCategoryRequest(
        @field:NotBlank
        val name: String?
)
