package br.pucpr.authserver.book.controller.requests

import br.pucpr.authserver.book.Stubs.bookStub
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CreateBookRequestTest {
    @Test
    fun `toBook creates a new book based on the request`() {
        with(bookStub()) {
            val req = CreateBookRequest(title, author, categories.map{it.toCategoryRequest()}.toMutableSet()).toBook()
            req.title shouldBe title
            req.author shouldBe author
            req.categories shouldBe categories
        }
    }
}