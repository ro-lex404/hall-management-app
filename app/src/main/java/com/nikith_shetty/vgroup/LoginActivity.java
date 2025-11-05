package com.nikith_shetty.vgroup;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

// Note: The fragments used here (loginFragment, registerFragment, forgotPasswordFragment)
// are based on the deprecated Stormpath SDK and will also need to be updated or replaced.

public class LoginActivity extends AppCompatActivity implements
        loginFragment.LoginFragmentListener,
        registerFragment.RegisterFragmentListener,
        forgotPasswordFragment.ResetPasswordFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.login_container, loginFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // This handles the user pressing the back arrow in the app bar.
        onBackPressed();
        return true;
    }

    @Override
    public void onRegisterClicked() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, registerFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onLoginSuccess() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onForgotPasswordClicked() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, forgotPasswordFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFragmentDone() {
        // This is called by child fragments to signal they are done,
        // so we pop the back stack to return to the login fragment.
        getSupportFragmentManager().popBackStack();
    }
}
