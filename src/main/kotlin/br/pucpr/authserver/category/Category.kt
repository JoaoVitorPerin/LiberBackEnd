package br.pucpr.authserver.category

import br.pucpr.authserver.book.Book
import br.pucpr.authserver.category.controller.requests.CategoryRequest
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "TblCategories")
class Category(
        @Id @GeneratedValue
        var id: Long? = null,

        @Column(unique = true)
        var name: String = "",

        @ManyToMany(mappedBy = "categories")
        @JsonIgnore
        val books: MutableSet<Book> = mutableSetOf()
) {
        fun toCategoryRequest() = CategoryRequest(
                name = name
        )
}