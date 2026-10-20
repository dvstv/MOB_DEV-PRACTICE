package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// экран 1 - афиша, выбор фильма и времени сеанса
class MoviesActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MoviesActivity"
    }

    private lateinit var btnCart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_movies)

        btnCart = findViewById(R.id.btnCart)
        btnCart.setOnClickListener {
            Log.i(TAG, "Нажата кнопка корзины, билетов: ${Cart.ticketCount()}")
            startActivity(Intent(this, CartActivity::class.java))
        }

        showMovies()
    }

    // карточка для каждого фильма и кнопка для каждого сеанса
    private fun showMovies() {
        val container = findViewById<LinearLayout>(R.id.movieContainer)
        val inflater = LayoutInflater.from(this)

        for (movie in MovieData.movies) {
            val card = inflater.inflate(R.layout.item_movie, container, false)
            card.findViewById<TextView>(R.id.tvTitle).text = movie.title
            card.findViewById<TextView>(R.id.tvInfo).text =
                getString(R.string.movie_info, movie.genre, movie.duration)
            card.findViewById<TextView>(R.id.tvPrice).text =
                getString(R.string.price_per_ticket, movie.price)

            val sessionContainer = card.findViewById<LinearLayout>(R.id.sessionContainer)
            for ((index, time) in movie.sessions.withIndex()) {
                val button = inflater.inflate(R.layout.item_session, sessionContainer, false) as Button
                button.text = time
                button.setOnClickListener { openSeats(movie, time, index + 1) }
                sessionContainer.addView(button)
            }
            container.addView(card)
        }
    }

    // явный Intent - переходим к выбору мест и передаём данные о сеансе
    private fun openSeats(movie: Movie, time: String, hall: Int) {
        Log.i(TAG, "Выбран сеанс: ${movie.title}, $time, зал $hall")
        val intent = Intent(this, SeatsActivity::class.java)
        intent.putExtra(SeatsActivity.EXTRA_TITLE, movie.title)
        intent.putExtra(SeatsActivity.EXTRA_TIME, time)
        intent.putExtra(SeatsActivity.EXTRA_HALL, hall)
        intent.putExtra(SeatsActivity.EXTRA_PRICE, movie.price)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        // количество билетов в корзине могло измениться
        btnCart.text = getString(R.string.btn_cart, Cart.ticketCount())
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}
