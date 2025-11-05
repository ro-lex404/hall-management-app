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
// import com.stormpath.sdk.models.StormpathError;

public class forgotPasswordFragment extends Fragment {

    private EditText email;
    private ProgressBar progressBar;
    private Button resetButton;
    private ResetPasswordFragmentListener mListener;

    public static forgotPasswordFragment newInstance() {
        return new forgotPasswordFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ResetPasswordFragmentListener) {
            mListener = (ResetPasswordFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ResetPasswordFragmentListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        email = view.findViewById(R.id.forgot_input_username);
        progressBar = view.findViewById(R.id.forgot_resetpw_progress_bar);
        resetButton = view.findViewById(R.id.forgot_resetpw_button);

        resetButton.setOnClickListener(v -> onSend());

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
        return view;
    }

    protected void onSend() {
        if (TextUtils.isEmpty(email.getText())) {
            Snackbar.make(resetButton, "Email field is mandatory", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // The original password reset logic was based on the Stormpath SDK, which is now deprecated.
        // This functionality needs to be reimplemented with a new authentication provider.
        Snackbar.make(resetButton, "Password reset functionality is not implemented.", Snackbar.LENGTH_LONG).show();

        // Since there is no reset functionality, we will just return to the previous screen.
        if (mListener != null) {
            mListener.onFragmentDone();
        }
    }

    public void showProgress() {
        resetButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        resetButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface ResetPasswordFragmentListener {
        void onFragmentDone();
    }
}
