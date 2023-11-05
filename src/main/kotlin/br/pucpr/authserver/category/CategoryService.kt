package br.pucpr.authserver.category

import br.pucpr.authserver.book.BookRepository
import br.pucpr.authserver.category.controller.responses.CategoryWithBooksResponse
import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService(
        @Autowired private val repository: CategoryRepository,
        @Autowired private val bookRepository: BookRepository
) {
    fun createCategory(category: Category): Category {
        if (repository.findByName(category.name) != null) {
            throw BadRequestException("Category already exists")
        }
        return repository.save(category)
                .also { log.info("Category inserted: {}", it.id) }
    }

    fun getAllCategories(): List<CategoryWithBooksResponse> {
        val categories = repository.findAll()
        return categories.map { category ->
            val books = bookRepository.findByCategory(category.name)
            CategoryWithBooksResponse(category.id!!, category.name, books)
        }
    }

    fun findCategoryByIdOrNull(id: Long): Category? =
            repository.findById(id).orElse(null)

    fun deleteCategory(id: Long): Boolean {
        val category = findCategoryByIdOrNull(id) ?: return false
        repository.delete(category)
        log.info("Category deleted: {}", category.id)
        return true
    }

    private fun findCategoryByIdOrThrow(id: Long): Category =
            findCategoryByIdOrNull(id) ?: throw NotFoundException(id)

    companion object {
        private val log = LoggerFactory.getLogger(UserService::class.java)
    }
}