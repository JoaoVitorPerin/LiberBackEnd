package br.pucpr.authserver.book.controller

import BookResponse
import br.pucpr.authserver.book.BookService
import br.pucpr.authserver.book.controller.requests.CreateBookRequest
import br.pucpr.authserver.book.controller.requests.PatchBookRequest
import br.pucpr.authserver.category.controller.requests.PatchCategoryRequest
import br.pucpr.authserver.category.controller.responses.CategoryResponse
import br.pucpr.authserver.exception.ForbiddenException
import br.pucpr.authserver.security.UserToken
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
        private val service: BookService
    ) {
    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @PostMapping
    fun create(@Valid @RequestBody request: CreateBookRequest): ResponseEntity<BookResponse> {
        val book = service.createBook(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(BookResponse(book))
    }

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @PatchMapping("/{id}")
    fun update(
            @PathVariable id: Long,
            @Valid @RequestBody request: PatchBookRequest,
            auth: Authentication
    ): ResponseEntity<BookResponse> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if (token.id != id && !token.isAdmin) throw ForbiddenException()

        return service.update(id, request)
                ?.let { ResponseEntity.ok(BookResponse(it)) }
                ?: ResponseEntity.noContent().build()
    }

    @GetMapping
    fun list() = service.getAllBooks().map { BookResponse(it) }.let { ResponseEntity.ok(it) }

    @GetMapping("/title/{title}")
    fun getByTitle(@PathVariable title: String? = null) =
            service.getBooksByTitle(title!!.uppercase()).map { BookResponse(it) }.let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
            service.findBookByIdOrNull(id)?.let { ResponseEntity.ok(BookResponse(it)) }
                    ?: ResponseEntity.notFound().build()

    @SecurityRequirement(name="AuthServer")
    @PreAuthorize("permitAll()")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long, auth: Authentication): ResponseEntity<Void> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if (token.id != id && !token.isAdmin) throw ForbiddenException()
        if (service.deleteBook(id)){
            return ResponseEntity.ok().build()
        } else {
            return ResponseEntity.notFound().build()
        }
    }


    companion object {
        private val log = LoggerFactory.getLogger(BookController::class.java)
    }
}
