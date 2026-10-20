package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// экран 3 - корзина с выбранными билетами
class CartActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "CartActivity"
        const val EXTRA_ADDED = "EXTRA_ADDED"
    }

    private lateinit var cartContainer: LinearLayout
    private lateinit var tvEmpty: TextView
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_cart)

        cartContainer = findViewById(R.id.cartContainer)
        tvEmpty = findViewById(R.id.tvEmpty)
        tvTotal = findViewById(R.id.tvTotal)

        // если пришли с экрана выбора мест, показываем, что было добавлено
        val added = intent.getStringExtra(EXTRA_ADDED)
        val tvAdded = findViewById<TextView>(R.id.tvAdded)
        if (added != null) {
            Log.d(TAG, "Получены данные: $added")
            tvAdded.text = getString(R.string.cart_added, added)
        } else {
            tvAdded.visibility = View.GONE
        }

        findViewById<Button>(R.id.btnAddMore).setOnClickListener {
            Log.i(TAG, "Нажата кнопка «Добавить ещё сеанс»")
            val intent = Intent(this, MoviesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnCheckout).setOnClickListener { checkout() }

        showCart()
    }

    private fun showCart() {
        cartContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for (item in Cart.items.toList()) {
            val card = inflater.inflate(R.layout.item_cart, cartContainer, false)
            card.findViewById<TextView>(R.id.tvItemTitle).text = item.movieTitle
            card.findViewById<TextView>(R.id.tvItemSession).text =
                getString(R.string.cart_item_session, item.time, item.hall)
            card.findViewById<TextView>(R.id.tvItemSeats).text =
                getString(R.string.cart_item_seats, formatSeats(item.seats))
            card.findViewById<TextView>(R.id.tvItemPrice).text =
                getString(R.string.cart_item_price, item.total)
            card.findViewById<Button>(R.id.btnRemove).setOnClickListener {
                Cart.items.remove(item)
                Log.i(TAG, "Удалён из корзины: ${item.movieTitle}, ${item.time}")
                showCart()
            }
            cartContainer.addView(card)
        }

        val empty = Cart.items.isEmpty()
        tvEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        tvTotal.text = getString(R.string.cart_total, Cart.total())
        Log.d(TAG, "В корзине позиций: ${Cart.items.size}, сумма ${Cart.total()}")
    }

    // "3-5" -> "р3 м5"
    private fun formatSeats(seats: List<String>): String =
        seats.joinToString(", ") { "р${it.substringBefore("-")} м${it.substringAfter("-")}" }

    private fun checkout() {
        if (Cart.items.isEmpty()) {
            Log.w(TAG, "Попытка оформить пустую корзину")
            Toast.makeText(this, R.string.toast_cart_empty, Toast.LENGTH_SHORT).show()
            return
        }
        Log.i(TAG, "Заказ оформлен, билетов: ${Cart.ticketCount()}, сумма ${Cart.total()}")
        Cart.items.clear()
        Toast.makeText(this, R.string.toast_checkout_done, Toast.LENGTH_LONG).show()

        val intent = Intent(this, MoviesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
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
