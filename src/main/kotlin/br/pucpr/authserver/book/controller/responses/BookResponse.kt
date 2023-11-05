import br.pucpr.authserver.book.Book
import br.pucpr.authserver.category.controller.responses.CategoryResponse

data class BookResponse(
        val id: Long,
        val title: String,
        val author: String,
        val categories: Set<CategoryResponse> // CategoryResponse deve ter um campo 'name' para representar a categoria
) {
    constructor(book: Book) : this(
            id = book.id!!,
            title = book.title,
            author = book.author,
            categories = book.categories.map { CategoryResponse(it.id!!, it.name!!) }.toSet()
    )
}