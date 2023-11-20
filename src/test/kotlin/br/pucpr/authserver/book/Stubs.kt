package br.pucpr.authserver.book

import br.pucpr.authserver.category.Category
import br.pucpr.authserver.category.controller.requests.CategoryRequest

object Stubs {
    fun bookStub(
            id: Long? = 1,
            titulo: String = "titulo",
            author: String = "autor",
            categoria: MutableSet<CategoryRequest> = mutableSetOf(CategoryRequest("nome")),

            ) = Book(
            id = id,
            title = titulo,
            author = author,
            categories = categoria.mapTo(mutableSetOf()) { it.toCategory() }
    )


}