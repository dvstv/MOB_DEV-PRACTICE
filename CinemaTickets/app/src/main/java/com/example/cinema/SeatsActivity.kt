package com.example.cinema

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

// Экран 2: схема зала и выбор мест
class SeatsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SeatsActivity"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_TIME = "EXTRA_TIME"
        const val EXTRA_HALL = "EXTRA_HALL"
        const val EXTRA_PRICE = "EXTRA_PRICE"
        private const val ROWS = 6
        private const val SEATS_IN_ROW = 8
    }

    private var title = ""
    private var time = ""
    private var hall = 1
    private var price = 0

    private val takenSeats = mutableSetOf<String>()     // занятые места
    private val selectedSeats = mutableSetOf<String>()  // выбранные пользователем
    private val seatViews = mutableMapOf<String, TextView>()

    private lateinit var tvSummary: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_seats)

        // Получаем данные, переданные с предыдущего экрана
        title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        time = intent.getStringExtra(EXTRA_TIME) ?: ""
        hall = intent.getIntExtra(EXTRA_HALL, 1)
        price = intent.getIntExtra(EXTRA_PRICE, 0)
        Log.d(TAG, "Получены данные: $title, $time, зал $hall, цена $price")

        findViewById<TextView>(R.id.tvMovieTitle).text = title
        findViewById<TextView>(R.id.tvSessionInfo).text =
            getString(R.string.seats_session_info, time, hall)
        tvSummary = findViewById(R.id.tvSummary)

        generateTakenSeats()
        buildSeatMap()
        updateSummary()

        findViewById<Button>(R.id.btnAddToCart).setOnClickListener { addToCart() }
    }

    // Занятые места: для одного и того же сеанса всегда одинаковые
    private fun generateTakenSeats() {
        val key = "$title|$time"
        val random = Random(key.hashCode().toLong())
        for (row in 1..ROWS) {
            for (seat in 1..SEATS_IN_ROW) {
                if (random.nextInt(100) < 25) {
                    takenSeats.add("$row-$seat")
                }
            }
        }
        // места, уже добавленные в корзину, тоже недоступны
        takenSeats.addAll(Cart.seatsInCart(key))
        Log.d(TAG, "Занято мест: ${takenSeats.size}")
    }

    private fun buildSeatMap() {
        val container = findViewById<LinearLayout>(R.id.seatContainer)
        val size = resources.getDimensionPixelSize(R.dimen.seat_size)
        val margin = resources.getDimensionPixelSize(R.dimen.seat_margin)
        val labelWidth = resources.getDimensionPixelSize(R.dimen.row_label_width)

        for (row in 1..ROWS) {
            val rowLayout = LinearLayout(this)
            rowLayout.orientation = LinearLayout.HORIZONTAL

            val label = TextView(this)
            label.text = getString(R.string.row_label, row)
            label.gravity = Gravity.CENTER
            label.layoutParams = LinearLayout.LayoutParams(labelWidth, size)
            rowLayout.addView(label)

            for (seat in 1..SEATS_IN_ROW) {
                val id = "$row-$seat"
                val seatView = TextView(this)
                seatView.text = seat.toString()
                seatView.gravity = Gravity.CENTER
                seatView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_seat))
                val params = LinearLayout.LayoutParams(size, size)
                params.setMargins(margin, margin, margin, margin)
                seatView.layoutParams = params
                seatView.setOnClickListener { onSeatClick(id) }

                seatViews[id] = seatView
                refreshSeat(id)
                rowLayout.addView(seatView)
            }
            container.addView(rowLayout)
        }
    }

    private fun onSeatClick(id: String) {
        if (takenSeats.contains(id)) {
            Log.w(TAG, "Попытка выбрать занятое место $id")
            Toast.makeText(this, R.string.toast_seat_taken, Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedSeats.contains(id)) {
            selectedSeats.remove(id)
            Log.i(TAG, "Место $id снято с выбора")
        } else {
            selectedSeats.add(id)
            Log.i(TAG, "Место $id выбрано")
        }
        refreshSeat(id)
        updateSummary()
    }

    // Меняем фон места в зависимости от состояния
    private fun refreshSeat(id: String) {
        val view = seatViews[id] ?: return
        when {
            takenSeats.contains(id) -> view.setBackgroundResource(R.drawable.seat_taken)
            selectedSeats.contains(id) -> view.setBackgroundResource(R.drawable.seat_selected)
            else -> view.setBackgroundResource(R.drawable.seat_free)
        }
    }

    private fun updateSummary() {
        tvSummary.text = getString(R.string.seats_summary, selectedSeats.size, selectedSeats.size * price)
    }

    private fun addToCart() {
        if (selectedSeats.isEmpty()) {
            Log.w(TAG, "Места не выбраны, добавление в корзину пропущено")
            Toast.makeText(this, R.string.toast_no_seats, Toast.LENGTH_SHORT).show()
            return
        }

        // сортируем места по ряду и номеру
        val seats = selectedSeats.sortedWith(compareBy(
            { it.substringBefore("-").toInt() },
            { it.substringAfter("-").toInt() }
        ))
        Cart.items.add(CartItem(title, time, hall, price, seats))
        Log.i(TAG, "В корзину добавлено: $title, $time, места $seats")

        // Явный Intent в корзину, передаём описание добавленного билета
        val intent = Intent(this, CartActivity::class.java)
        intent.putExtra(CartActivity.EXTRA_ADDED, "$title, $time (${seats.size} шт.)")
        startActivity(intent)
        finish()
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
