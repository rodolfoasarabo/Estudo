package com.poppin.movies.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.poppin.movies.R;
import com.poppin.movies.retrofit.MoviesService;
import com.poppin.movies.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseActivity extends AppCompatActivity {

    public int refLayout;
    Retrofit retrofit;
    public MoviesService service;
    public Context ctx;
    Snackbar semRede;
    CoordinatorLayout v;

    private BroadcastReceiver mudancaConectividade = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mudancaConexao();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(refLayout);
        ctx = this;
        hideKeyboard();
        configRetrofit();
        v = findViewById(R.id.coordinatorLayout);
        semRede = Snackbar.make(v, "Sem conexao", Snackbar.LENGTH_INDEFINITE)
                .setAction("Configurar", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ctx.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mudancaConexao();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mudancaConectividade, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mudancaConectividade);
    }

    private void configRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MoviesService.class);
    }

    private void mudancaConexao() {
        if (!isConnected(this)) {
            semRede.show();
        } else {
            semRede.dismiss();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }

        return false;
    }

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            View view = getCurrentFocus();
            view.clearFocus();
            CoordinatorLayout v = findViewById(R.id.coordinatorLayout);
            v.requestFocus();
        }
    }

    public void showLoader(View v, boolean visible) {
        v.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
