package com.ardian.pacitanku.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.di.component.ActivityComponent;
import com.ardian.pacitanku.di.component.DaggerActivityComponent;
import com.ardian.pacitanku.di.module.ActivityModule;
import com.ardian.pacitanku.ui.activity.home.HomeActivity;
import com.ardian.pacitanku.ui.activity.register.RegisterActivity;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

// aktivitas untuk halaman login
public class LoginActivity extends AppCompatActivity implements LoginActivityContract.View {

    // presenter yg diinject ke activity
    @Inject
    public LoginActivityContract.Presenter presenter;

    // deklarasi konteks
    private Context context;

    // form login yang digunakan
    private EditText email,password;

    // tombol yang digunakan
    private Button login,register;

    // fungsi yg dipanggil saat activity
    // dibuat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // panggil fungsi init widget
        initWidget();
    }

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    private void initWidget(){
        this.context = this;

        // memanggil fungsi inject
        injectDependency();

        // ke activity ini
        presenter.attach(this);

        // subscribe presenter
        presenter.subscribe();

        // inisialisasi form email
        this.email = findViewById(R.id.login_email_edittext);

        // inisialisasi form password
        this.password = findViewById(R.id.login_password_edittext);

        // inisialisasi tombol login
        this.login = findViewById(R.id.login_button);

        // saat diklik
        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validasi form login
                if (email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    Toast.makeText(context,getString(R.string.empty_login),Toast.LENGTH_SHORT).show();
                    return;
                }

                // panggil proses kirim form login
                presenter.login(email.getText().toString(),password.getText().toString(),true);
            }
        });

        // inisialisasi tombol register
        this.register = findViewById(R.id.register_button);

        // saat diklik
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // panggil aktivitas registrasi
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });

        // check sesi login
        presenter.checkSession();
    }

    // progres login
    @Override
    public void showProgressLogin(Boolean show) {

    }

    // error saat login
    @Override
    public void showErrorLogin(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    // pada saat berhasil login
    @Override
    public void onLogin(@NonNull FirebaseUser user) {

        // kosongkan form login
        email.setText("");password.setText("");

        // ke activitas home
        startActivity(new Intent(context, HomeActivity.class));

        // hancurkan aktivitas saat ini
        finish();
    }

    // jika ada sesi
    @Override
    public void onLoginSession(@NonNull FirebaseUser user) {

        // ke activitas home
        startActivity(new Intent(context, HomeActivity.class));

        // hancurkan aktivitas saat ini
        finish();
    }

    // fungsi yg akan dipanggil saat activity
    // dihancurkan
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }


    // pemanggilan register
    // dependensi injeksi untuk aktivity ini
    private void injectDependency(){
        ActivityComponent listcomponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .build();

        listcomponent.inject(this);
    }
}