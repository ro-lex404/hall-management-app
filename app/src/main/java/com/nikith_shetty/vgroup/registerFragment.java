package com.nikith_shetty.vgroup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

// Stormpath SDK is deprecated. All related code has been commented out.
// import com.stormpath.sdk.Stormpath;
// import com.stormpath.sdk.StormpathCallback;
// import com.stormpath.sdk.models.RegisterParams;
// import com.stormpath.sdk.models.StormpathError;

public class registerFragment extends Fragment {

    private View view;
    private RegisterFragmentListener registerFragmentListener;
    private EditText firstName;
    private EditText surname;
    private EditText email;
    private EditText password;
    private Button registerButton;
    private ProgressBar progressBar;

    public static registerFragment newInstance() {
        return new registerFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragmentListener) {
            registerFragmentListener = (RegisterFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterFragmentListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        firstName = view.findViewById(R.id.register_input_firstname);
        surname = view.findViewById(R.id.register_input_surname);
        password = view.findViewById(R.id.register_input_password);
        progressBar = view.findViewById(R.id.register_register_progress_bar);
        email = view.findViewById(R.id.register_input_email);
        registerButton = view.findViewById(R.id.register_register_button);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    email.getBackground().clearColorFilter();
                } else {
                    email.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registerButton.setOnClickListener(v -> onRegisterClicked());

        return view;
    }

    private void onRegisterClicked() {
        if (TextUtils.isEmpty(firstName.getText())
                || TextUtils.isEmpty(surname.getText())
                || TextUtils.isEmpty(email.getText())
                || TextUtils.isEmpty(password.getText())) {
            Snackbar.make(registerButton, "All fields are mandatory", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()) {
            Snackbar.make(registerButton, "A valid email is required", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // The original registration logic was based on the Stormpath SDK, which is now deprecated.
        // This functionality needs to be reimplemented with a new authentication provider.
        Snackbar.make(registerButton, "Registration functionality is not implemented.", Snackbar.LENGTH_LONG).show();

        // Since there is no registration, we will just return to the previous screen.
        if (registerFragmentListener != null) {
            registerFragmentListener.onFragmentDone();
        }
    }

    public interface RegisterFragmentListener {
        void onFragmentDone();
    }

    public void showProgress() {
        registerButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        registerButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        registerFragmentListener = null;
    }
}
