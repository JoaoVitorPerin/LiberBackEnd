package br.pucpr.authserver.category

import BookResponse
import br.pucpr.authserver.book.BookRepository
import br.pucpr.authserver.category.controller.responses.CategoryWithBooksResponse
import br.pucpr.authserver.exception.BadRequestException
import br.pucpr.authserver.exception.NotFoundException
import br.pucpr.authserver.users.SortDir
import org.springframework.data.domain.Sort
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

    fun getAllCategories(dir: SortDir = SortDir.ASC): List<CategoryWithBooksResponse> {
        val categories = when (dir) {
            SortDir.ASC -> repository.findAll(Sort.by("name").ascending())
            SortDir.DESC -> repository.findAll(Sort.by("name").descending())
        }

        return categories.map { category ->
            val books = bookRepository.findByCategory(category.name).map { BookResponse(it) }.toMutableSet()
            CategoryWithBooksResponse(category.id!!, category.name, books)
        }
    }


    fun findCategoryByIdOrNull(id: Long): Category? =
            repository.findById(id).orElse(null)

    fun findCategoryByNameOrNull(name: String): Category? {
        return repository.findByName(name)
    }

    private fun findByIdOrThrow(id: Long) =
            findCategoryByIdOrNull(id) ?: throw NotFoundException(id)

    fun update(id: Long, name: String): Category? {
        val category = findByIdOrThrow(id)
        if (category.name == name) return null
        category.name = name
        return repository.save(category)
    }

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