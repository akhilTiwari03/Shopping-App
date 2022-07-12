package com.example.splashactivity;

import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }
    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FrameLayout parentFrameLayout;

    private TextView signUp;

    private EditText signIn_email;
    private EditText signIn_password;
    private Button signIn_now;
    private ImageButton closeButton;
    private TextView forgotpassword;

    private FirebaseAuth firebaseAuth;

    private ProgressBar progressBar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in,container,false);
        signUp = view.findViewById(R.id.signUp);
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);
        signIn_email = view.findViewById(R.id.sign_in_email);
        signIn_password = view.findViewById(R.id.sign_in_password);
        signIn_now = view.findViewById(R.id.signIn_now);
        closeButton = view.findViewById(R.id.signIn_close_button);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.signIn_progressBar);
        forgotpassword = view.findViewById(R.id.forget_password);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainIntent();
            }
        });
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new ResetPasswordFragment());
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignUpFragment());
            }
        });
        signIn_email.addTextChangedListener(new TextWatcher() {
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
        signIn_password.addTextChangedListener(new TextWatcher() {
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
        signIn_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailandPassword();
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction  fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(signIn_email.getText())) {
            if(!TextUtils.isEmpty(signIn_password.getText())) {
                signIn_now.setEnabled(true);
                signIn_now.setTextColor(Color.rgb(255,255,255));
            }
            else
            {
                signIn_now.setEnabled(false);
                signIn_now.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else {
            signIn_now.setEnabled(false);
            signIn_now.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void checkEmailandPassword() {
        if(signIn_email.getText().toString().matches(emailPattern)) {
            if(signIn_password.length() >= 8) {
                progressBar.setVisibility(View.VISIBLE);
                signIn_now.setEnabled(false);
                signIn_now.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.signInWithEmailAndPassword(signIn_email.getText().toString(), signIn_password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                   mainIntent();
                                }
                                else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signIn_now.setEnabled(true);
                                    signIn_now.setTextColor(Color.rgb(255,255,255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                   //Toast.makeText(getActivity(), "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
            else {
                Toast.makeText(getActivity(), "Incorrect Email or Password!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getActivity(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
        }
    }

    private void mainIntent() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}