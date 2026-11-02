package com.example.lab3g2_lifecycle

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class LogObserver : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        Log.d("LC_OBS", "start")
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.d("LC_OBS", "stop")
    }
}