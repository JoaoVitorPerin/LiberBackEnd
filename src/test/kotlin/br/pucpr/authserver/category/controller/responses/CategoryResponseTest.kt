package br.pucpr.authserver.book.controller.responses

import BookResponse
import br.pucpr.authserver.book.Stubs.bookStub
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CategoryResponseTest {
    @Test
    fun `constructor should copy all important values`() {
        val book = bookStub()
        val response = BookResponse(book)
        response.id shouldBe book.id
        response.title shouldBe book.title
        response.author shouldBe book.author
        response.categories shouldBe book.categories
    }
}