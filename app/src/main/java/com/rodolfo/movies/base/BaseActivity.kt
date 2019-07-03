package com.rodolfo.movies.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.rodolfo.movies.R
import com.rodolfo.movies.retrofit.Requests
import com.rodolfo.movies.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseActivity : AppCompatActivity() {

    var refLayout: Int = 0
    lateinit var retrofit: Retrofit
    lateinit var service: Requests
    lateinit var ctx: Context
    lateinit var semRede: Snackbar
    lateinit var v: androidx.coordinatorlayout.widget.CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(refLayout)
        ctx = this
        hideKeyboard()
        configRetrofit()
        v = findViewById(R.id.coordinatorLayout)
        semRede = Snackbar.make(v, "Sem conexao", Snackbar.LENGTH_INDEFINITE)
                .setAction("Configurar") { ctx.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }
    }

    override fun onResume() {
        super.onResume()
        mudancaConexao()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mudancaConectividade, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(mudancaConectividade)
    }

    private val mudancaConectividade = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mudancaConexao()
        }
    }

    private fun configRetrofit() {
        retrofit = Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        service = retrofit.create(Requests::class.java)
    }

    private fun mudancaConexao() {
        if (!isConnected(this)) {
            semRede.show()
        } else {
            semRede.dismiss()
        }
    }

    private fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        cm.let {
            val ni = it.activeNetworkInfo
            return ni != null && ni.isConnected
        }
    }

    fun hideKeyboard() {
        currentFocus?.let {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val view = it
            view.clearFocus()
            val v = findViewById<androidx.coordinatorlayout.widget.CoordinatorLayout>(R.id.coordinatorLayout)
            v.requestFocus()
        }
    }

    fun showLoader(v: View, visible: Boolean) {
        v.visibility = if (visible) View.VISIBLE else View.GONE
    }
}