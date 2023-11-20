package br.pucpr.authserver.category.controller.requests

import br.pucpr.authserver.category.Stubs.categoryStub
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CreateCategoryRequestTest {
    @Test
    fun `toCategory creates a new category based on the request`() {
        with(categoryStub()) {
            val req = CreateCategoryRequest(name).toCategory()
            req.name shouldBe name
        }
    }
}