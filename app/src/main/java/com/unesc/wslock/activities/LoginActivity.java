package com.unesc.wslock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.unesc.wslock.R;
import com.unesc.wslock.dtos.AuthDTO;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.Auth;
import com.unesc.wslock.services.AuthService;
import com.unesc.wslock.services.BaseService;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText loginInput;
    private TextInputEditText passwordInput;
    private MaterialButton signInButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.loginInput = findViewById(R.id.login_input);
        this.passwordInput = findViewById(R.id.password_input);
        this.signInButton = findViewById(R.id.sign_in_button);
        this.progressBar = findViewById(R.id.login_progress_bar);

        this.progressBar.setVisibility(View.GONE);

        this.checkAuthenticatedUser();

        this.signInButton.setOnClickListener(v -> {
            if (checkRequiredFields()) {
                login();
            }
        });

        LinearLayout createAccount = findViewById(R.id.create_account_linear_layout);

        createAccount.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, UserFormActivity.class)));
    }

    private void checkAuthenticatedUser() {
        if (AuthenticatedUser.getToken(LoginActivity.this) != null) {
            this.refreshToken();
        }
    }

    private void refreshToken() {
        AuthService authService = BaseService.getRetrofitInstance().create(AuthService.class);
        Call<Auth> request = authService.refresh(AuthenticatedUser.getToken(LoginActivity.this));

        request.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                if (response.isSuccessful()) {
                    AuthenticatedUser.saveUserData(response.body().getUser(), response.body().getAccessToken(), LoginActivity.this);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Sessão expirada", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Sessão expirada", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void login() {
        showProgressBar();

        AuthService authService = BaseService.getRetrofitInstance().create(AuthService.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), this.makeAuthDTO().toJson());
        Call<Auth> request = authService.login(body);

        request.enqueue(new Callback<Auth>() {
            @Override
            public void onResponse(Call<Auth> call, Response<Auth> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    AuthenticatedUser.saveUserData(response.body().getUser(), response.body().getAccessToken(), LoginActivity.this);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciais incorretas", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Auth> call, Throwable t) {
                hideProgressBar();

                Toast.makeText(LoginActivity.this, "Houve um erro ao realizar o login", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkRequiredFields() {
        if (this.loginInput.getText().toString().isEmpty()) {
            this.loginInput.setError("Campo obrigatório");
        }

        if (this.passwordInput.getText().toString().isEmpty()) {
            this.passwordInput.setError("Campo obrigatório");
        }

        return !this.loginInput.getText().toString().isEmpty() && !this.passwordInput.getText().toString().isEmpty();
    }

    private AuthDTO makeAuthDTO() {
        AuthDTO authDTO = new AuthDTO();

        authDTO.setLogin(this.loginInput.getText().toString());
        authDTO.setPassword(this.passwordInput.getText().toString());

        return authDTO;
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.signInButton.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
        this.signInButton.setVisibility(View.VISIBLE);
    }
}