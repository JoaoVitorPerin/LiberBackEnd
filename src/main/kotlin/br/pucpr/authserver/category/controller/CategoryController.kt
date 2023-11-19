package br.pucpr.authserver.category.controller

import BookResponse
import br.pucpr.authserver.category.CategoryService
import br.pucpr.authserver.category.controller.requests.CreateCategoryRequest
import br.pucpr.authserver.category.controller.requests.PatchCategoryRequest
import br.pucpr.authserver.category.controller.responses.CategoryResponse
import br.pucpr.authserver.category.controller.responses.CategoryWithBooksResponse
import br.pucpr.authserver.exception.ForbiddenException
import br.pucpr.authserver.security.UserToken
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.controller.requests.PatchUserRequest
import br.pucpr.authserver.users.controller.responses.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(private val service: CategoryService) {

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @PostMapping
    fun create(@Valid @RequestBody request: CreateCategoryRequest): ResponseEntity<CategoryResponse> {
        val category = service.createCategory(request.toCategory())
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse(category))
    }

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @PatchMapping("/{id}")
    fun update(
            @PathVariable id: Long,
            @Valid @RequestBody request: PatchCategoryRequest,
            auth: Authentication
    ): ResponseEntity<CategoryResponse> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if (token.id != id && !token.isAdmin) throw ForbiddenException()

        return service.update(id, request.name!!)
                ?.let { ResponseEntity.ok(CategoryResponse(it)) }
                ?: ResponseEntity.noContent().build()
    }

    @GetMapping
    fun list(@RequestParam sortDir: String? = null): ResponseEntity<List<CategoryWithBooksResponse>> {
        val categoriesWithBooks = service.getAllCategories(SortDir.findOrThrow(sortDir ?: "ASC"))
        val categories = categoriesWithBooks.map {
            CategoryWithBooksResponse(it.id!!, it.name!!, it.books!!)
        }
        return ResponseEntity.ok(categories)
    }


    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<CategoryWithBooksResponse> {
        val category = service.findCategoryByIdOrNull(id)
        return if (category != null) {
            val bookResponses: MutableSet<BookResponse> = category.books.map { BookResponse(it) }.toMutableSet()
            val categoryWithBooksResponse = CategoryWithBooksResponse(
                    category.id!!,
                    category.name,
                    bookResponses
            )
            ResponseEntity.ok(categoryWithBooksResponse)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long, auth: Authentication): ResponseEntity<Void> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if (token.id != id && !token.isAdmin) throw ForbiddenException()
        if (service.deleteCategory(id)){
            return ResponseEntity.ok().build()
        } else{
            return ResponseEntity.notFound().build()
        }
    }


    companion object {
        private val log = LoggerFactory.getLogger(CategoryController::class.java)
    }
}
