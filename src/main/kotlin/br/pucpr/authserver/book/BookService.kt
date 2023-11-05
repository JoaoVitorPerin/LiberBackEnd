package br.pucpr.authserver.book

import br.pucpr.authserver.book.controller.requests.CreateBookRequest
import br.pucpr.authserver.category.Category
import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.category.CategoryRepository
import br.pucpr.authserver.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class BookService(
        @Autowired private val repository: BookRepository,
        @Autowired private val categoryRepository: CategoryRepository
) {

    fun createBook(request: CreateBookRequest): Book {
        val categories: MutableSet<Category> = mutableSetOf()

        // Verifica se as categorias existem no banco de dados e as salva se necessário
        request.categories.forEach { categoryRequest ->
            val existingCategory = categoryRepository.findByName(categoryRequest.name)
            val category = existingCategory ?: Category(name = categoryRequest.name)
            categories.add(categoryRepository.save(category))
        }

        // Cria o livro com as categorias já persistidas
        val book = Book(
                title = request.title,
                author = request.author,
                categories = categories
        )

        // Salva o livro no banco de dados
        return repository.save(book)
                .also { savedBook -> log.info("Book inserted: {}", savedBook.id) }
    }

    fun updateBook(id: Long, title: String): Book? {
        val book = findBookByIdOrThrow(id)
        if (book.title == title) return null
        book.title = title
        return repository.save(book)
    }

    fun getAllBooks(): List<Book> =
            repository.findAll(Sort.by("title").ascending())

    fun getBooksByTitle(title: String): List<Book> =
            repository.findBooksByTitle(title)

    fun getBooksByCategory(category: String): List<Book> {
        val categoryEntity = categoryRepository.findByName(category) ?: throw NotFoundException("Category not found")
        return categoryRepository.findBooksByCategoryName(categoryEntity.name)
    }


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
