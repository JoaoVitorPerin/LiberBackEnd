package br.pucpr.authserver.book

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long> {
    fun findByTitle(title: String): Book?

    @Query("select distinct b from Book b" +
            " join b.categories c" +
            " where c.name = :category" +
            " order by b.title")
    fun findByCategory(category: String): List<Book>
}