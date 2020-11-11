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

public class RegisterActivity extends AppCompatActivity implements RegisterActivityContract.View {

    @Inject
    public RegisterActivityContract.Presenter presenter;

    private Context context;

    private ImageView back;
    private EditText email;
    private EditText username;
    private EditText password;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidget();
    }

    private void initWidget(){
        this.context = this;

        injectDependency();
        presenter.attach(this);
        presenter.subscribe();

        this.email = findViewById(R.id.register_email_edittext);
        this.username = findViewById(R.id.register_username_edittext);
        this.password = findViewById(R.id.register_password_edittext);

        this.register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().isEmpty() || email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    Toast.makeText(context,getString(R.string.empty_register),Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.register(email.getText().toString(),password.getText().toString(),true);
            }
        });
        this.back = findViewById(R.id.back_imageview);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void showProgressRegister(Boolean show) {

    }

    @Override
    public void showErrorRegister(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegister(@NonNull FirebaseUser user) {
        presenter.update(username.getText().toString(),true);
    }

    @Override
    public void showProgressUpdate(Boolean show) {

    }

    @Override
    public void showErrorUpdate(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(@NonNull FirebaseUser user) {
        email.setText("");password.setText("");username.setText("");
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
    }


    private void injectDependency(){
        ActivityComponent listcomponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .build();

        listcomponent.inject(this);
    }

}