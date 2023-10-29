package br.pucpr.authserver.category.controller

import br.pucpr.authserver.category.CategoryService
import br.pucpr.authserver.category.controller.requests.CreateCategoryRequest
import br.pucpr.authserver.category.controller.responses.CategoryResponse
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
class CategoryController(private val service: CategoryService) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateCategoryRequest): ResponseEntity<CategoryResponse> {
        val category = service.createCategory(request.toCategory())
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoryResponse(category))
    }

    @GetMapping
    fun list() =
            service.getAllCategories().map { CategoryResponse(it) }.let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
            service.findCategoryByIdOrNull(id)?.let { ResponseEntity.ok(CategoryResponse(it)) }
                    ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
            if (service.deleteCategory(id)) ResponseEntity.ok().build()
            else ResponseEntity.notFound().build()

    companion object {
        private val log = LoggerFactory.getLogger(CategoryController::class.java)
    }
}
