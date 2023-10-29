package br.pucpr.authserver.category.controller.responses

import br.pucpr.authserver.category.Category

data class CategoryResponse(
        val id: Long,
        val name: String
) {
    constructor(category: Category) : this(category.id!!, category.name)
}
