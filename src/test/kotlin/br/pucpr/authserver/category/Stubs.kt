package br.pucpr.authserver.category

import br.pucpr.authserver.category.Category

object Stubs {
    fun categoryStub(
            id: Long? = 1,
            name: String = "name",

            ) = Category(
            id = id,
            name = name,
    )


}