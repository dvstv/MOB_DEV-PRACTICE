package com.example.cinema

// фильм в афише, данные задаются в коде
data class Movie(
    val title: String,
    val genre: String,
    val duration: String,
    val price: Int,          // цена одного билета в тенге
    val sessions: List<String>
)

object MovieData {
    val movies = listOf(
        Movie("Горбатая гора", "Комедия", "2 ч 10 мин", 2500, listOf("10:00", "13:30", "18:00", "21:15")),
        Movie("Зеленый слоник", "Драма", "1 ч 55 мин", 2200, listOf("11:00", "15:00", "19:30")),
        Movie("Терминатор", "Фэнтези", "1 ч 40 мин", 2000, listOf("12:00", "16:30", "20:00")),
        Movie("Бизнес по Казахски", "Комедия", "2 ч 05 мин", 2600, listOf("14:00", "17:45", "22:00")),
        Movie("Спанчбоб", "Мультфильм", "1 ч 30 мин", 1800, listOf("09:30", "11:30", "14:30"))
    )
}
