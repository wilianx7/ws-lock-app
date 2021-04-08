package com.unesc.wslock.localstorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.unesc.wslock.models.User;

public class AuthenticatedUser {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;

    private static final String ID_KEY = "authenticated_user_id";
    private static final String LOGIN_KEY = "authenticated_user_login";
    private static final String EMAIL_KEY = "authenticated_user_email";
    private static final String NAME_KEY = "authenticated_user_name";
    private static final String TOKEN_KEY = "authenticated_user_token";

    public static void saveUserData(User user, String token, Context context) {
        getSharedPreferencesEditor(context).putString(ID_KEY, String.valueOf(user.getId()));
        getSharedPreferencesEditor(context).putString(LOGIN_KEY, user.getLogin());
        getSharedPreferencesEditor(context).putString(EMAIL_KEY, user.getEmail());
        getSharedPreferencesEditor(context).putString(NAME_KEY, user.getName());

        if (token != null) {
            getSharedPreferencesEditor(context).putString(TOKEN_KEY, token);
        }

        getSharedPreferencesEditor(context).commit();
    }

    public static void deleteUserData(Context context) {
        getSharedPreferencesEditor(context).clear();
        getSharedPreferencesEditor(context).commit();
    }

    public static User getUser(Context context) {
        User user = new User();

        user.setId(Integer.parseInt(getSharedPreferences(context).getString(ID_KEY, null)));
        user.setLogin(getSharedPreferences(context).getString(LOGIN_KEY, null));
        user.setEmail(getSharedPreferences(context).getString(EMAIL_KEY, null));
        user.setName(getSharedPreferences(context).getString(NAME_KEY, null));

        return user;
    }

    public static String getToken(Context context) {
        String token = getSharedPreferences(context).getString(TOKEN_KEY, null);

        if (token != null) {
            return "Bearer" + token;
        }

        return null;
    }

    private static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        if (sharedPreferencesEditor == null) {
            sharedPreferencesEditor = getSharedPreferences(context).edit();
        }

        return sharedPreferencesEditor;
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("ws_lock.authenticated_user_data", Context.MODE_PRIVATE);
        }

        return sharedPreferences;
    }
}
