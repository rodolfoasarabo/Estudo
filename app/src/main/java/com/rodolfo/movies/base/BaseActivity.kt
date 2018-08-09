package com.rodolfo.movies.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.rodolfo.movies.R
import com.rodolfo.movies.retrofit.MoviesService
import com.rodolfo.movies.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseActivity : AppCompatActivity() {

    var refLayout: Int = 0
    lateinit var retrofit: Retrofit
    lateinit var service: MoviesService
    lateinit var ctx: Context
    lateinit var semRede: Snackbar
    lateinit var v: CoordinatorLayout

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

        service = retrofit.create(MoviesService::class.java)
    }

    private fun mudancaConexao() {
        if (!isConnected(this)) {
            semRede.show()
        } else {
            semRede.dismiss()
        }
    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (cm != null) {
            val ni = cm.activeNetworkInfo
            return ni != null && ni.isConnected
        }

        return false
    }

    fun hideKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            val view = currentFocus
            view!!.clearFocus()
            val v = findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
            v.requestFocus()
        }
    }

    fun showLoader(v: View, visible: Boolean) {
        v.visibility = if (visible) View.VISIBLE else View.GONE
    }
}