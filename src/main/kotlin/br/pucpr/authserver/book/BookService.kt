package br.pucpr.authserver.book

import br.pucpr.authserver.book.controller.requests.CreateBookRequest
import br.pucpr.authserver.book.controller.requests.PatchBookRequest
import br.pucpr.authserver.category.Category
import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.category.CategoryRepository
import br.pucpr.authserver.category.CategoryService
import br.pucpr.authserver.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class BookService(
        @Autowired private val repository: BookRepository,
        @Autowired private val categoryRepository: CategoryRepository,
        @Autowired private val categoryService: CategoryService,
) {

    fun createBook(request: CreateBookRequest): Book {
        val categories: MutableSet<Category> = mutableSetOf()

        // Verifica se as categorias existem no banco de dados e as salva se necessário
        request.categories.forEach { categoryRequest ->
            val existingCategory = categoryRepository.findByName(categoryRequest.name)
            val category = existingCategory ?: Category(name = categoryRequest.name)
            categories.add(categoryRepository.save(category).also { log.info("Category inserted: {}", category.id) })
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

    fun update(id: Long, newBook: PatchBookRequest): Book? {
        val book = findBookByIdOrThrow(id)
        if (book.title == newBook.title) return null
        book.title = newBook.title
        book.author = newBook.author

        // Limpe as categorias existentes
        book.categories.clear()

        // Verifique se cada categoria tem um nome não vazio
        for (categoryRequest in newBook.categories) {
            if (categoryRequest.name.isBlank()) {
                return null
            }

            // Tente encontrar a categoria existente pelo nome
            val existingCategory: Category? = categoryService.findCategoryByNameOrNull(categoryRequest.name)

            // Se a categoria existir, use-a; caso contrário, crie uma nova categoria
            val category: Category = existingCategory ?: Category(name = categoryRequest.name)

            // Salve a categoria se for uma nova
            if (existingCategory == null) {
                val savedCategory: Category = categoryService.createCategory(category)
                log.info("Category inserted: {}", savedCategory.id)
                book.categories.add(savedCategory)
            } else {
                // Adicione a categoria existente ao livro
                book.categories.add(category)
            }
        }

        return repository.save(book)
    }

    fun getAllBooks(): List<Book> =
            repository.findAll(Sort.by("title").ascending())

    fun getBooksByTitle(title: String): List<Book> =
            repository.findBooksByTitle(title)

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
