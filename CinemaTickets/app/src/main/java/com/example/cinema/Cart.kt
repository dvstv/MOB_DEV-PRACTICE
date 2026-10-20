package com.example.cinema

// один пункт корзины - фильм, сеанс и выбранные места
data class CartItem(
    val movieTitle: String,
    val time: String,
    val hall: Int,
    val price: Int,
    val seats: List<String>   // места в виде "ряд-место", например "3-5"
) {
    val total: Int get() = price * seats.size
    val sessionKey: String get() = "$movieTitle|$time"
}

// корзина хранится в памяти пока приложение запущено
object Cart {
    val items = mutableListOf<CartItem>()

    fun total(): Int = items.sumOf { it.total }

    fun ticketCount(): Int = items.sumOf { it.seats.size }

    // места которые уже лежат в корзине для этого сеанса
    fun seatsInCart(sessionKey: String): Set<String> =
        items.filter { it.sessionKey == sessionKey }.flatMap { it.seats }.toSet()
}
