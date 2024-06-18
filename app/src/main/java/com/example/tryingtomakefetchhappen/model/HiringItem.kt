package com.example.tryingtomakefetchhappen.model

data class HiringItem(
    val id: Int,
    val listId: Int,
    val name: String?
) {
    override fun toString(): String {
        val n = if (name != null) "\"$name\"" else null
        return "HiringItem(id=$id, listId=$listId, name=$n)"
    }
}