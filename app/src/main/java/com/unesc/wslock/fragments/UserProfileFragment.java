package com.unesc.wslock.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.unesc.wslock.R;
import com.unesc.wslock.activities.MainActivity;
import com.unesc.wslock.dtos.ValidationDTO;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.User;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.UserService;
import com.unesc.wslock.services.ValidationService;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    private User user;
    private TextInputEditText nameInput;
    private TextInputEditText loginInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText passwordConfirmationInput;
    private MaterialButton saveButton;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        this.nameInput = view.findViewById(R.id.name_input);
        this.loginInput = view.findViewById(R.id.login_input);
        this.emailInput = view.findViewById(R.id.email_input);
        this.passwordInput = view.findViewById(R.id.new_password_input);
        this.passwordConfirmationInput = view.findViewById(R.id.password_confirmation_input);
        this.saveButton = view.findViewById(R.id.save_user_button);
        this.progressBar = view.findViewById(R.id.user_profile_progress_bar);

        this.bindUserToEdit();

        this.saveButton.setOnClickListener(v -> {
            showProgressBar();

            if (checkRequiredFields()) {
                checkLoginUnique();
            } else {
                hideProgressBar();
            }
        });

        return view;
    }

    private void save() {
        this.syncUserAttributes();

        UserService userService = BaseService.getRetrofitInstance().create(UserService.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), this.user.toJson());
        Call<User> request = userService.update(AuthenticatedUser.getToken(getContext()), body);

        request.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Salvo com sucesso!", Toast.LENGTH_SHORT).show();

                    AuthenticatedUser.saveUserData(user, null, getContext());

                    startActivity(new Intent(getContext(), MainActivity.class));
                } else {
                    Toast.makeText(getContext(), "Houve um erro ao salvar seu perfil", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressBar();

                Toast.makeText(getContext(), "Houve um erro ao salvar seu perfil", Toast.LENGTH_LONG).show();
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

        if (!this.passwordInput.getText().toString().isEmpty()) {
            if (this.passwordInput.getText().toString().length() < 8) {
                this.passwordInput.setError("Deve conter no mínimo 8 caracteres");
            }

            if (this.passwordConfirmationInput.getText().toString().isEmpty()) {
                this.passwordConfirmationInput.setError("Campo obrigatório");
            } else if (!this.passwordInput.getText().toString().equals(this.passwordConfirmationInput.getText().toString())) {
                this.passwordConfirmationInput.setError("As senhas não coincidem");
            }
        }

        return !this.nameInput.getText().toString().isEmpty()
                && !this.emailInput.getText().toString().isEmpty()
                && !this.loginInput.getText().toString().isEmpty()
                && (
                this.passwordInput.getText().toString().isEmpty()
                        || (!this.passwordInput.getText().toString().isEmpty()
                        && !this.passwordConfirmationInput.getText().toString().isEmpty()
                        && this.passwordInput.getText().toString().equals(this.passwordConfirmationInput.getText().toString()))
        );
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
                Toast.makeText(getContext(), "Houve um erro ao cadastrar o usuário", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(), "Houve um erro ao cadastrar o usuário", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bindUserToEdit() {
        this.user = AuthenticatedUser.getUser(getContext());

        this.nameInput.setText(this.user.getName());
        this.emailInput.setText(this.user.getEmail());
        this.loginInput.setText(this.user.getLogin());
    }

    private void syncUserAttributes() {
        this.user.setName(nameInput.getText().toString());
        this.user.setLogin(loginInput.getText().toString());
        this.user.setEmail(emailInput.getText().toString());

        if (!passwordInput.getText().toString().isEmpty()) {
            this.user.setPassword(passwordInput.getText().toString());
        }
    }

    private ValidationDTO makeValidationForLogin() {
        ValidationDTO validationDTO = new ValidationDTO();

        validationDTO.setTable("users");
        validationDTO.setColumn("login");
        validationDTO.setValue(this.loginInput.getText().toString());
        validationDTO.setIgnore(String.valueOf(this.user.getId()));

        return validationDTO;
    }

    private ValidationDTO makeValidationForEmail() {
        ValidationDTO validationDTO = new ValidationDTO();

        validationDTO.setTable("users");
        validationDTO.setColumn("email");
        validationDTO.setValue(this.emailInput.getText().toString());
        validationDTO.setIgnore(String.valueOf(this.user.getId()));

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