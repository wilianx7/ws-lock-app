package com.unesc.wslock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.unesc.wslock.R;
import com.unesc.wslock.dtos.ValidationDTO;
import com.unesc.wslock.models.User;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.UserService;
import com.unesc.wslock.services.ValidationService;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFormActivity extends AppCompatActivity {
    private TextInputEditText nameInput;
    private TextInputEditText loginInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText passwordConfirmationInput;
    private MaterialButton saveButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        this.nameInput = findViewById(R.id.name_input);
        this.loginInput = findViewById(R.id.login_input);
        this.emailInput = findViewById(R.id.email_input);
        this.passwordInput = findViewById(R.id.password_input);
        this.passwordConfirmationInput = findViewById(R.id.password_confirmation_input);
        this.saveButton = findViewById(R.id.save_user_button);
        this.progressBar = findViewById(R.id.user_form_progress_bar);

        Toolbar toolbar = findViewById(R.id.user_form_toolbar);

        toolbar.setTitle("Cadastro de Usuário");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);

        this.saveButton.setOnClickListener(v -> {
            showProgressBar();

            if (checkRequiredFields()) {
                checkLoginUnique();
            } else {
                hideProgressBar();
            }
        });
    }

    private void save() {
        UserService userService = BaseService.getRetrofitInstance().create(UserService.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), this.makeUserForSave().toJson());
        Call<User> request = userService.create(body);

        request.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    Toast.makeText(UserFormActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(UserFormActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(UserFormActivity.this, "Houve um erro ao cadastrar o usuário", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressBar();

                Toast.makeText(UserFormActivity.this, "Houve um erro ao cadastrar o usuário", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkRequiredFields() {
        if (this.nameInput.getText().toString().isEmpty()) {
            this.nameInput.setError("Campo obrigatório");
        }

        if (this.emailInput.getText().toString().isEmpty()) {
            this.emailInput.setError("Campo obrigatório");
        }

        if (this.loginInput.getText().toString().isEmpty()) {
            this.loginInput.setError("Campo obrigatório");
        }

        if (this.passwordInput.getText().toString().isEmpty()) {
            this.passwordInput.setError("Campo obrigatório");
        } else if (this.passwordInput.getText().toString().length() < 8) {
            this.passwordInput.setError("Deve conter no mínimo 8 caracteres");
        }

        if (this.passwordConfirmationInput.getText().toString().isEmpty()) {
            this.passwordConfirmationInput.setError("Campo obrigatório");
        } else if (!this.passwordInput.getText().toString().equals(this.passwordConfirmationInput.getText().toString())) {
            this.passwordConfirmationInput.setError("As senhas não coincidem");
        }

        return !this.nameInput.getText().toString().isEmpty()
                && !this.emailInput.getText().toString().isEmpty()
                && !this.loginInput.getText().toString().isEmpty()
                && !this.passwordInput.getText().toString().isEmpty()
                && !this.passwordConfirmationInput.getText().toString().isEmpty()
                && this.passwordInput.getText().toString().equals(this.passwordConfirmationInput.getText().toString());
    }

    private void checkLoginUnique() {
        ValidationService validationService = BaseService.getRetrofitInstance().create(ValidationService.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), this.makeValidationForLogin().toJson());
        Call<String> request = validationService.unique(body);

        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("0")) {
                    loginInput.setError("Usuário já cadastrado");
                }

                checkEmailUnique();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UserFormActivity.this, "Houve um erro ao cadastrar o usuário", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkEmailUnique() {
        ValidationService validationService = BaseService.getRetrofitInstance().create(ValidationService.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), this.makeValidationForEmail().toJson());
        Call<String> request = validationService.unique(body);

        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("0")) {
                    hideProgressBar();
                    emailInput.setError("E-mail já cadastrado");
                } else if (loginInput.getError() == null) {
                    save();
                } else {
                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UserFormActivity.this, "Houve um erro ao cadastrar o usuário", Toast.LENGTH_LONG).show();
            }
        });
    }

    private User makeUserForSave() {
        User user = new User();

        user.setName(nameInput.getText().toString());
        user.setLogin(loginInput.getText().toString());
        user.setEmail(emailInput.getText().toString());
        user.setPassword(passwordInput.getText().toString());

        return user;
    }

    private ValidationDTO makeValidationForLogin() {
        ValidationDTO validationDTO = new ValidationDTO();

        validationDTO.setTable("users");
        validationDTO.setColumn("login");
        validationDTO.setValue(this.loginInput.getText().toString());

        return validationDTO;
    }

    private ValidationDTO makeValidationForEmail() {
        ValidationDTO validationDTO = new ValidationDTO();

        validationDTO.setTable("users");
        validationDTO.setColumn("email");
        validationDTO.setValue(this.emailInput.getText().toString());

        return validationDTO;
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
        this.saveButton.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
        this.saveButton.setVisibility(View.VISIBLE);
    }
}