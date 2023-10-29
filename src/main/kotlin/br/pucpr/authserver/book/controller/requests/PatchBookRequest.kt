package br.pucpr.authserver.book.controller.requests

import jakarta.validation.constraints.NotBlank

data class PatchBookRequest(
        @field:NotBlank
        val title: String?
)