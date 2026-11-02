package com.example.lab3g2_lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : LoggingActivity() {

    // PART C3: Fix with ViewModel
    private val vm: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PART E1: Register LogObserver
        lifecycle.addObserver(LogObserver())

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // PART D2: Log the previously saved value
        Log.d(
            "LC_PREF",
            "saved = ${getSharedPreferences("lab", MODE_PRIVATE).getInt("saved", -1)}"
        )

        val tvCount = findViewById<TextView>(R.id.tvCount)

        // PART C3: Display count using ViewModel
        tvCount.text = "Count: ${vm.count}"

        // PART C2: Restore the previous count
        // count = savedInstanceState?.getInt("count") ?: 0
        // tvCount.text = "Count :$count"

        val btnPlus = findViewById<Button>(R.id.btnPlus)
        val btnDialog = findViewById<Button>(R.id.btnDialog)
        val btnSecondScreen = findViewById<Button>(R.id.btnSecondScreen)
        val btnTransparentScreen = findViewById<Button>(R.id.btnTransparentScreen)
        val btnClose = findViewById<Button>(R.id.btnClose)

        btnPlus.setOnClickListener {
            vm.count++
            tvCount.text = "Count: ${vm.count}"
        }

        btnDialog.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Dialog")
                .setMessage("See Logcat")
                .setPositiveButton("OK", null)
                .show()
        }

        btnSecondScreen.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        btnTransparentScreen.setOnClickListener {
            val intent = Intent(this, TransparentActivity::class.java)
            startActivity(intent)
        }

        btnClose.setOnClickListener {
            finish()
        }

        // PART E3: Collect ticker while Activity is STARTED
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ticker().collect {
                    Log.d("LC_FLOW", "tick $it")
                }
            }
        }
    }

    // PART E2 / E3: Ticker function
    private fun ticker() = flow {
        var i = 0
        while (true) {
            emit(i++)
            delay(1000)
        }
    }

    // PART C2: Save the current count
    // override fun onSaveInstanceState(outState: Bundle) {
    //     super.onSaveInstanceState(outState)
    //     outState.putInt("count", count)
    // }

    // PART D2: Save count to SharedPreferences
    override fun onStop() {
        super.onStop()

        getSharedPreferences("lab", MODE_PRIVATE)
            .edit()
            .putInt("saved", vm.count)
            .apply()
    }
}