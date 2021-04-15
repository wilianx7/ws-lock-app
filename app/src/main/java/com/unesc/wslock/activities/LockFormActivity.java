package com.unesc.wslock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.unesc.wslock.R;
import com.unesc.wslock.adapters.LockFormUserSelectListAdapter;
import com.unesc.wslock.localstorage.AuthenticatedUser;
import com.unesc.wslock.models.Lock;
import com.unesc.wslock.models.User;
import com.unesc.wslock.models.lists.UserList;
import com.unesc.wslock.services.BaseService;
import com.unesc.wslock.services.LockService;
import com.unesc.wslock.services.UserService;
import com.unesc.wslock.utils.MaskWatcher;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LockFormActivity extends AppCompatActivity {
    private Lock lock = new Lock();
    private TextInputEditText nameInput;
    private TextInputEditText macAddressInput;
    private TextInputLayout macAddressInputLayout;
    private MaterialButton saveButton;
    private ProgressBar progressBar;
    private TextView userSelectLabel;
    private ListView userSelectListView;
    private LockFormUserSelectListAdapter userSelectAdapter;
    private Toolbar toolbar;

    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_form);

        this.nameInput = findViewById(R.id.lock_form_name_input);
        this.macAddressInput = findViewById(R.id.lock_form_mac_address_input);
        this.macAddressInputLayout = findViewById(R.id.lock_form_mac_address_input_layout);
        this.saveButton = findViewById(R.id.lock_form_save_button);
        this.progressBar = findViewById(R.id.lock_form_progress_bar);
        this.userSelectListView = findViewById(R.id.lock_form_users_list);
        this.userSelectLabel = findViewById(R.id.lock_form_users_select_label);
        this.toolbar = findViewById(R.id.lock_form_toolbar);

        this.macAddressInput.addTextChangedListener(new MaskWatcher("##:##:##:##:##:##"));

        this.checkIntentExtras();

        this.configureToolbar();

        if (!this.isEditing || this.isLockOwner()) {
            this.loadUsers();
        }

        this.handleUserSelectListViewClick();

        this.saveButton.setOnClickListener(v -> {
            if (checkRequiredFields()) {
                try {
                    save();
                } catch (JSONException e) {
                    hideProgressBar();
                    e.printStackTrace();
                }
            } else {
                hideProgressBar();
            }
        });
    }

    private void save() throws JSONException {
        showProgressBar();

        LockService lockService = BaseService.getRetrofitInstance().create(LockService.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), this.makeLockForSave().toJson());
        Call<Lock> request = lockService.createOrUpdate(AuthenticatedUser.getToken(LockFormActivity.this), body);

        request.enqueue(new Callback<Lock>() {
            @Override
            public void onResponse(Call<Lock> call, Response<Lock> response) {
                hideProgressBar();

                if (response.isSuccessful()) {
                    Toast.makeText(LockFormActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LockFormActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LockFormActivity.this, "Código já cadastrado!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Lock> call, Throwable t) {
                hideProgressBar();

                Toast.makeText(LockFormActivity.this, "Houve um erro ao salvar a fechadura", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUsers() {
        this.showProgressBar();

        UserService userService = BaseService.getRetrofitInstance().create(UserService.class);
        Call<UserList> request = userService.index(AuthenticatedUser.getToken(LockFormActivity.this), null, this.lock.getId());

        request.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if (response.isSuccessful()) {
                    UserList responseBody = response.body();

                    if (responseBody != null && responseBody.users.size() > 0) {
                        userSelectAdapter = new LockFormUserSelectListAdapter(LockFormActivity.this, responseBody);

                        userSelectListView.setAdapter(userSelectAdapter);
                    }

                    hideProgressBar();
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                hideProgressBar();

                Toast.makeText(LockFormActivity.this, "Houve um erro ao carregar os usuários", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleUserSelectListViewClick() {
        this.userSelectListView.setOnItemClickListener((parent, view, position, id) -> {
            User user = userSelectAdapter.getData().get(position);
            user.setHasLockAccess(!user.hasLockAccess());

            userSelectAdapter.notifyDataSetChanged();
        });
    }

    private Lock makeLockForSave() {
        this.lock.setName(this.nameInput.getText().toString());

        if (!this.isEditing || this.isLockOwner()) {
            this.lock.setMacAddress(this.macAddressInput.getText().toString());

            if (this.userSelectAdapter != null) {
                List<User> selectedUsers = new ArrayList<>();

                for (User user : this.userSelectAdapter.getData()) {
                    if (user.hasLockAccess()) {
                        selectedUsers.add(user);
                    }
                }

                this.lock.setUsers(selectedUsers);
            }
        }

        return this.lock;
    }

    private void checkIntentExtras() {
        Intent intent = getIntent();
        Lock lock = (Lock) intent.getSerializableExtra("lock");

        if (lock != null) {
            this.isEditing = true;
            this.lock = lock;

            this.nameInput.setText(this.lock.getName());

            if (!this.isLockOwner()) {
                this.macAddressInput.setVisibility(View.GONE);
                this.macAddressInputLayout.setVisibility(View.GONE);
                this.userSelectLabel.setVisibility(View.GONE);
                this.userSelectListView.setVisibility(View.GONE);
            } else {
                this.macAddressInput.setText(this.lock.getMacAddress());
                this.macAddressInput.setEnabled(false);
            }
        }
    }

    private boolean isLockOwner() {
        return this.lock.getCreatedByUserId() == AuthenticatedUser.getUser(LockFormActivity.this).getId();
    }

    private void configureToolbar() {
        if (this.isEditing) {
            toolbar.setTitle("Edição de Fechadura");
        } else {
            toolbar.setTitle("Cadastro de Fechadura");
        }

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        setSupportActionBar(toolbar);
    }

    private boolean checkRequiredFields() {
        if (this.nameInput.getText().toString().isEmpty()) {
            this.nameInput.setError("Campo obrigatório");
        }

        if (!this.isEditing && this.macAddressInput.getText().toString().isEmpty()) {
            this.macAddressInput.setError("Campo obrigatório");
        }

        return !this.nameInput.getText().toString().isEmpty() && (!this.macAddressInput.getText().toString().isEmpty() || this.isEditing);
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