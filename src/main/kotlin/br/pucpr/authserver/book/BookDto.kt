package br.pucpr.authserver.book

import br.pucpr.authserver.category.CategoryDto

data class BookDto(
    var id: Long?,
    var title: String,
    var author: String,
    var categories: Set<CategoryDto>
)