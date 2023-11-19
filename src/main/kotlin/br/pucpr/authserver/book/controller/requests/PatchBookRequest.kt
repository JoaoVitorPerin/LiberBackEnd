package br.pucpr.authserver.book.controller.requests

import br.pucpr.authserver.category.Category
import br.pucpr.authserver.category.controller.requests.CategoryRequest
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class PatchBookRequest(
        @field:NotBlank
        var title: String = "",

        @field:NotBlank
        var author: String = "",

        @field:NotEmpty
        val categories: MutableSet<CategoryRequest> = mutableSetOf()

)