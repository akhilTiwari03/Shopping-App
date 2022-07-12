package com.example.splashactivity;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPasswordFragment extends Fragment {
    private FrameLayout parentFrameLayout;
    private EditText forgot_password_email;
    private Button forgot_password_send_email;
    private ImageButton forgot_password_go_back;
    private TextView alreadyHaveAccountSignIn;

    private FirebaseAuth firebaseAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_forgot_password, container, false);
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);
        forgot_password_email = view.findViewById(R.id.forgot_email);
        forgot_password_send_email = view.findViewById(R.id.forgot_button);
        forgot_password_go_back = view.findViewById(R.id.forgot_back_button);
        alreadyHaveAccountSignIn = view.findViewById(R.id.forgot_signIn_button);
        firebaseAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forgot_password_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        forgot_password_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
        alreadyHaveAccountSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
        forgot_password_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgot_password_send_email.setEnabled(false);
                forgot_password_send_email.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.sendPasswordResetEmail(forgot_password_email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "email sent successfully", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error+"", Toast.LENGTH_SHORT).show();
                                    }
                                forgot_password_send_email.setEnabled(true);
                                forgot_password_send_email.setTextColor(Color.rgb(255,255,255));
                            }
                        });
            }
        });

    }

    private void checkInputs() {
        if(TextUtils.isEmpty(forgot_password_email.getText())) {
            forgot_password_send_email.setEnabled(false);
            forgot_password_send_email.setTextColor(Color.argb(50,255,255,255));
        }
        else {
            forgot_password_send_email.setEnabled(true);
            forgot_password_send_email.setTextColor(Color.rgb(255,255,255));
        }
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}