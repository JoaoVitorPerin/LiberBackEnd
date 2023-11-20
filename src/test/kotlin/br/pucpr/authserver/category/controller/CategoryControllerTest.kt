package br.pucpr.authserver.category.controller

import br.pucpr.authserver.category.controller.responses.CategoryResponse
import br.pucpr.authserver.category.Stubs.categoryStub
import br.pucpr.authserver.category.CategoryService
import br.pucpr.authserver.category.controller.requests.CreateCategoryRequest

import io.kotest.matchers.shouldBe
import io.mockk.checkUnnecessaryStub
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class CategoryControllerTest {
    private val serviceMock = mockk<CategoryService>()
    private val controller = CategoryController(serviceMock)

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @AfterEach
    fun cleanUp() {
        checkUnnecessaryStub(serviceMock)
    }

    @Test
    fun `insert must return the new category with CREATED code`() {
        val category = categoryStub(id=null)

        val request = CreateCategoryRequest(category.name)
        every { serviceMock.createCategory(any()) } returns category
        with(controller.create(request)) {
            statusCode shouldBe HttpStatus.CREATED
            body shouldBe CategoryResponse(category)
        }
    }
}