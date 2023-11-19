package br.pucpr.authserver.book

import br.pucpr.authserver.category.Category
import jakarta.persistence.*

@Entity
@Table(name = "TblBooks")
class Book(
        @Id @GeneratedValue
        var id: Long? = null,

        @Column(unique = true)
        var title: String = "",

        var author: String = "",

        @ManyToMany
        @JoinTable(
                name = "BookCategory",
                joinColumns = [JoinColumn(name = "bookId")],
                inverseJoinColumns = [JoinColumn(name = "categoryId")]
        )
        var categories: MutableSet<Category> = mutableSetOf()
) {
}