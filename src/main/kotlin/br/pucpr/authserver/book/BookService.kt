package br.pucpr.authserver.book

import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class BookService(
        @Autowired private val repository: BookRepository
) {

    fun createBook(book: Book): Book {
        if (repository.findByTitle(book.title) != null) {
            throw BadRequestException("Book already exists")
        }
        return repository.save(book)
                .also { log.info("Book inserted: {}", it.id) }
    }

    fun updateBook(id: Long, title: String): Book? {
        val book = findBookByIdOrThrow(id)
        if (book.title == title) return null
        book.title = title
        return repository.save(book)
    }

    fun getAllBooks(): List<Book> =
        repository.findAll(Sort.by("title").ascending())

    fun getBooksByCategory(category: String): List<Book> =
            repository.findByCategory(category)

    fun findBookByIdOrNull(id: Long): Book? =
            repository.findById(id).orElse(null)

    fun deleteBook(id: Long): Boolean {
        val book = findBookByIdOrNull(id) ?: return false
        repository.delete(book)
        log.info("Book deleted: {}", book.id)
        return true
    }

    private fun findBookByIdOrThrow(id: Long): Book =
            findBookByIdOrNull(id) ?: throw NotFoundException(id)

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
}
