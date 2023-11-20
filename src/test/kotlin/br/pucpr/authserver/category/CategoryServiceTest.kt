package br.pucpr.authserver.category

import br.pucpr.authserver.book.Book
import br.pucpr.authserver.book.Stubs
import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.security.Jwt
import br.pucpr.authserver.category.Stubs.categoryStub
import br.pucpr.authserver.category.controller.requests.CreateCategoryRequest
import br.pucpr.authserver.category.CategoryRepository
import br.pucpr.authserver.book.BookRepository
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

internal class CategoryServiceTest {
    private val repositoryMock = mockk<CategoryRepository>()
    private val categoryRepositoryMock = mockk<CategoryRepository>()
    private val bookRepositoryMock = mockk<BookRepository>()
    private val jwt = mockk<Jwt>()

    private val service = CategoryService(repositoryMock, bookRepositoryMock)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp() {
        checkUnnecessaryStub(repositoryMock, categoryRepositoryMock, jwt)
    }

    @Test
    fun `insert must throw BadRequestException if an category with the same title is found`() {
        val category = categoryStub(id=null)

        every { repositoryMock.findByName(category.name) } returns categoryStub()
        assertThrows<BadRequestException> {
            service.createCategory(category)
        } shouldHaveMessage "Category already exists"
    }


    @Test
    fun `update must return null if there's no changes`() {
        val category = categoryStub()
        every { repositoryMock.findById(1) } returns Optional.of(category)
        service.update(1, "name") shouldBe null
    }


    @Test
    fun `update update and save the category with slot and capture`() {
        val category = categoryStub()
        every { repositoryMock.findById(1) } returns Optional.of(category)

        val saved = categoryStub(1, "name")
        val slot = slot<Category>()
        every { repositoryMock.save(capture(slot)) } returns saved

        service.update(1, "name") shouldBe saved
        slot.isCaptured shouldBe true
        slot.captured.name shouldBe "name"
    }

    @Test
    fun `update update and save the category `() {
        every { repositoryMock.findById(1) } returns Optional.of(categoryStub())
        every { repositoryMock.save(any()) } answers { firstArg() }

        val saved = service.update(1, "name")!!
        saved.name shouldBe "name"
    }



    @Test
    fun `findCategoryByIdOrNull should delegate to repository`() {
        val category = categoryStub()
        every { repositoryMock.findById(1) } returns Optional.of(category)
        service.findCategoryByIdOrNull(1) shouldBe category
    }

    @Test
    fun `delete must return false if the user does not exists`() {
        every { repositoryMock.findById(1) } returns Optional.empty()
        service.deleteCategory(1) shouldBe false
    }

    @Test
    fun `delete must call delete and return true if the category exists`() {
        val category = categoryStub()
        every { repositoryMock.findById(1) } returns Optional.of(category)
        justRun { repositoryMock.delete(category) }
        service.deleteCategory(1) shouldBe true
    }


}