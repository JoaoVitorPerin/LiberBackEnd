package br.pucpr.authserver.book.controller

import BookResponse
import br.pucpr.authserver.book.Stubs.bookStub
import br.pucpr.authserver.book.BookService
import br.pucpr.authserver.book.controller.requests.CreateBookRequest
import br.pucpr.authserver.category.controller.requests.CategoryRequest

import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class BookControllerTest {
    private val serviceMock = mockk<BookService>()
    private val controller = BookController(serviceMock)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp() {
        checkUnnecessaryStub(serviceMock)
    }

    @Test
    fun `insert must return the new book with CREATED code`() {
        val book = bookStub(id=null)

        val request = CreateBookRequest("titulo", "autor", mutableSetOf<CategoryRequest>(CategoryRequest("aaaaa")))
        every { serviceMock.createBook(any()) } returns book
        with(controller.create(request)) {
            statusCode shouldBe HttpStatus.CREATED
            body shouldBe BookResponse(book)
        }
    }
}