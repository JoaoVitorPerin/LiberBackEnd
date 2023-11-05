package br.pucpr.authserver.book.controller

import BookResponse
import br.pucpr.authserver.book.BookService
import br.pucpr.authserver.book.controller.requests.CreateBookRequest
import br.pucpr.authserver.book.controller.requests.PatchBookRequest
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
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

    @PatchMapping("/{id}")
    fun update(
            @PathVariable id: Long,
            @Valid @RequestBody request: PatchBookRequest
    ): ResponseEntity<BookResponse> {
        val book = service.updateBook(id, request.title!!)
        return book?.let { ResponseEntity.ok(BookResponse(it)) } ?: ResponseEntity.noContent().build()
    }
    @GetMapping
    fun list() = service.getAllBooks().map { BookResponse(it) }.let { ResponseEntity.ok(it) }

    // TODO: implementar get por title 
    @GetMapping("/title/{title}")
    fun getByTitle(@PathVariable title: String? = null) =
            service.getBooksByTitle(title!!.uppercase()).map { BookResponse(it) }.let { ResponseEntity.ok(it) }

    @GetMapping("/category/{category}")
    fun getByCategory(@PathVariable category: String? = null) =
                service.getBooksByCategory(category!!.uppercase()).map { BookResponse(it) }.let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long) =
            service.findBookByIdOrNull(id)?.let { ResponseEntity.ok(BookResponse(it)) }
                    ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
            if (service.deleteBook(id)) ResponseEntity.ok().build()
            else ResponseEntity.notFound().build()

    companion object {
        private val log = LoggerFactory.getLogger(BookController::class.java)
    }
}
