package com.example.lab3g2_lifecycle

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

class CounterViewModel(
    private val state: SavedStateHandle
) : ViewModel() {

    //C3: Previous ViewModel implementation
    // var count = 0

    // D1: SavedStateHandle implementation
    var count: Int
        get() = state["count"] ?: 0
        set(value) {
            state["count"] = value
        }

    init {
        Log.d("LC_VM", "ViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("LC_VM", "ViewModel destroyed")
    }
}