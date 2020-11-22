package com.ardian.pacitanku.ui.activity.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.di.component.ActivityComponent;
import com.ardian.pacitanku.di.component.DaggerActivityComponent;
import com.ardian.pacitanku.di.module.ActivityModule;
import com.ardian.pacitanku.ui.activity.home.HomeActivity;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

// aktivitas untuk halaman register
public class RegisterActivity extends AppCompatActivity implements RegisterActivityContract.View {

    // presenter yg diinject ke activity
    @Inject
    public RegisterActivityContract.Presenter presenter;

    // deklarasi konteks
    private Context context;

    // tombol kembali
    private ImageView back;

    // form register yang digunakan
    private EditText email,username, password;

    // tombol register yang digunakan
    private Button register;

    // fungsi yg dipanggil saat activity
    // dibuat
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

        // inisialisasi email
        this.email = findViewById(R.id.register_email_edittext);

        // inisialisasi username
        this.username = findViewById(R.id.register_username_edittext);

        // inisialisasi password
        this.password = findViewById(R.id.register_password_edittext);

        // inisialisasi tombol register
        this.register = findViewById(R.id.register_button);

        // pada saat diklik
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // validasi form register
                if (username.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    Toast.makeText(context,getString(R.string.empty_register),Toast.LENGTH_SHORT).show();
                    return;
                }

                // panggil proses kirim form register
                presenter.register(email.getText().toString(),password.getText().toString(),true);
            }
        });

        // inisialisasi tombol kembali
        this.back = findViewById(R.id.back_imageview);

        // pada saat diklik
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // hancurkan aktivitas saat ini
                finish();
            }
        });

    }

    // progress saat register
    @Override
    public void showProgressRegister(Boolean show) {

    }

    // error saat register
    @Override
    public void showErrorRegister(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    // register sukses
    @Override
    public void onRegister(@NonNull FirebaseUser user) {

        // update username
        presenter.update(username.getText().toString(),true);
    }

    // progress saat update
    @Override
    public void showProgressUpdate(Boolean show) {

    }

    // error saat update
    @Override
    public void showErrorUpdate(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    // saat berhasil update
    @Override
    public void onUpdate(@NonNull FirebaseUser user) {

        // kosongkan form register
        email.setText("");password.setText("");username.setText("");

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