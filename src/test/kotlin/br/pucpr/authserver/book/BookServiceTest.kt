package br.pucpr.authserver.book

import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.security.Jwt
import br.pucpr.authserver.book.Stubs.bookStub
import br.pucpr.authserver.book.controller.requests.CreateBookRequest
import br.pucpr.authserver.book.controller.requests.PatchBookRequest
import br.pucpr.authserver.category.CategoryRepository
import br.pucpr.authserver.category.CategoryService
import br.pucpr.authserver.category.controller.requests.CategoryRequest
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.Stubs
import br.pucpr.authserver.users.User
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Sort
import java.util.*

internal class BookServiceTest {
    private val repositoryMock = mockk<BookRepository>()
    private val categoryRepositoryMock = mockk<CategoryRepository>()
    private  val categoryService = mockk<CategoryService>()
    private val jwt = mockk<Jwt>()

    private val service = BookService(repositoryMock, categoryRepositoryMock, categoryService)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp() {
        checkUnnecessaryStub(repositoryMock, categoryRepositoryMock, jwt)
    }

    @Test
    fun `insert must throw BadRequestException if an book with the same title is found`() {
        val book = bookStub(id=null)

        every { repositoryMock.findByTitle(book.title) } returns bookStub()
        assertThrows<BadRequestException> {
            service.createBook(book.toBookRequest())
        } shouldHaveMessage "Book already exists"
    }

    @Test
    fun `update must return null if there's no changes`() {
        val book = bookStub()
        every { repositoryMock.findById(1) } returns Optional.of(book)
        service.update(1, PatchBookRequest("titulo", "autor", mutableSetOf<CategoryRequest>(CategoryRequest("aaaaa")))) shouldBe null
    }


    @Test
    fun `update update and save the book with slot and capture`() {
        val book = bookStub()
        every { repositoryMock.findById(1) } returns Optional.of(book)

        val saved = bookStub(1, "titulo")
        val slot = slot<Book>()
        every { repositoryMock.save(capture(slot)) } returns saved

        service.update(1, PatchBookRequest("titulo", "autor", mutableSetOf<CategoryRequest>(CategoryRequest("aaaaa")))) shouldBe saved
        slot.isCaptured shouldBe true
        slot.captured.title shouldBe "titulo"
    }

    @Test
    fun `update update and save the book `() {
        every { repositoryMock.findById(1) } returns Optional.of(bookStub())
        every { repositoryMock.save(any()) } answers { firstArg() }

        val saved = service.update(1, PatchBookRequest("titulo"))!!
        saved.title shouldBe "titulo"
    }



    @Test
    fun `findBookByIdOrNull should delegate to repository`() {
        val book = bookStub()
        every { repositoryMock.findById(1) } returns Optional.of(book)
        service.findBookByIdOrNull(1) shouldBe book
    }

    @Test
    fun `delete must return false if the user does not exists`() {
        every { repositoryMock.findById(1) } returns Optional.empty()
        service.deleteBook(1) shouldBe false
    }

    @Test
    fun `delete must call delete and return true if the book exists`() {
        val book = bookStub()
        every { repositoryMock.findById(1) } returns Optional.of(book)
        justRun { repositoryMock.delete(book) }
        service.deleteBook(1) shouldBe true
    }



}