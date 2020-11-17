package com.ardian.pacitanku.ui.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ardian.pacitanku.R;
import com.ardian.pacitanku.di.component.ActivityComponent;
import com.ardian.pacitanku.di.component.DaggerActivityComponent;
import com.ardian.pacitanku.di.module.ActivityModule;
import com.ardian.pacitanku.model.userType.UserType;
import com.ardian.pacitanku.service.FirebaseNotificationService;
import com.ardian.pacitanku.ui.activity.home.HomeActivity;
import com.ardian.pacitanku.ui.activity.register.RegisterActivity;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import static com.ardian.pacitanku.util.CheckService.isMyServiceRunning;

public class LoginActivity extends AppCompatActivity implements LoginActivityContract.View {

    @Inject
    public LoginActivityContract.Presenter presenter;

    private Context context;

    private EditText email;
    private EditText password;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initWidget();
    }

    private void initWidget(){
        this.context = this;

        injectDependency();
        presenter.attach(this);
        presenter.subscribe();

        this.email = findViewById(R.id.login_email_edittext);
        this.password = findViewById(R.id.login_password_edittext);
        this.login = findViewById(R.id.login_button);
        this.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()){
                    Toast.makeText(context,getString(R.string.empty_login),Toast.LENGTH_SHORT).show();
                    return;
                }
                presenter.login(email.getText().toString(),password.getText().toString(),true);
            }
        });
        this.register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, RegisterActivity.class));
            }
        });
        presenter.checkSession();
    }

    @Override
    public void showProgressLogin(Boolean show) {

    }

    @Override
    public void showErrorLogin(String error) {
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogin(@NonNull FirebaseUser user) {
        email.setText("");password.setText("");
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }

    @Override
    public void onLoginSession(@NonNull FirebaseUser user) {
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