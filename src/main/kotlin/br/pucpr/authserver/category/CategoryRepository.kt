package br.pucpr.authserver.category

import br.pucpr.authserver.book.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByName(name: String): Category?

    @Query("select distinct c.books from Category c where lower(c.name) like lower(concat('%', :categoryName, '%'))")
    fun findBooksByCategoryName(@Param("categoryName") categoryName: String): List<Book>
}