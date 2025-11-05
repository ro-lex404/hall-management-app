package com.nikith_shetty.vgroup;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

// Stormpath SDK is deprecated and its services have been discontinued.
// All related code has been commented out and needs to be replaced
// with a new authentication provider.
// import com.stormpath.sdk.Stormpath;
// import com.stormpath.sdk.StormpathCallback;
// import com.stormpath.sdk.models.StormpathError;
// import com.stormpath.sdk.models.UserProfile;

/**
 * Created by Nikith_Shetty on 28/04/2016.
 */
public class loginFragment extends Fragment {
    private View view;
    private EditText userName;
    private EditText password;
    private Button loginButton;
    private ProgressDialog loginProgress;
    private LoginFragmentListener loginFragmentListener;

    public static loginFragment newInstance(){
        return new loginFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentListener) {
            loginFragmentListener = (LoginFragmentListener) context;
        } else {
            throw new IllegalArgumentException("Activity must implement LoginFragmentListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        userName = view.findViewById(R.id.login_username);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> onLoginButtonClicked());

        Button registerButton = view.findViewById(R.id.login_register_button);
        registerButton.setOnClickListener(v -> onRegisterButtonClicked());

        Button forgotPassword = view.findViewById(R.id.login_forgot_password);
        forgotPassword.setOnClickListener(v -> onForgotPasswordClicked());
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (loginProgress != null && loginProgress.isShowing()) {
            loginProgress.dismiss();
        }
        loginProgress = null;
    }

    private void onLoginButtonClicked() {
        // The original login logic was based on the Stormpath SDK, which is now deprecated.
        // This functionality needs to be reimplemented with a new authentication provider.
        Snackbar.make(loginButton, "Login functionality is not implemented.", Snackbar.LENGTH_LONG).show();
    }

    private void onRegisterButtonClicked() {
        if (loginFragmentListener != null) {
            loginFragmentListener.onRegisterClicked();
        }
    }


    private void onForgotPasswordClicked() {
        if (loginFragmentListener != null) {
            loginFragmentListener.onForgotPasswordClicked();
        }
    }

    // This interface must be implemented by the hosting Activity (LoginActivity).
    public interface LoginFragmentListener {
        void onRegisterClicked();
        void onLoginSuccess();
        void onForgotPasswordClicked();
    }
}
