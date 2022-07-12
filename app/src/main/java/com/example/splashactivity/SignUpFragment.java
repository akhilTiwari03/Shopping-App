package com.example.splashactivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignUpFragment extends Fragment {

    private TextView signIn;
    private FrameLayout parentFrameLayout;
    private EditText fullname;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;

    private ImageButton closebutton;
    private Button signupButton;

    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailPattern =  "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up,container,false);
        signIn = view.findViewById(R.id.signIn);
        parentFrameLayout = getActivity().findViewById(R.id.register_frameLayout);
        fullname = view.findViewById(R.id.signUp_name);
        email = view.findViewById(R.id.sign_up_email);
        password = view.findViewById(R.id.sign_up_password);
        confirmPassword = view.findViewById(R.id.signUp_confirm_password);
        closebutton = view.findViewById(R.id.signUp_close_button);
        signupButton = view.findViewById(R.id.signUp_now);
        progressBar = view.findViewById(R.id.signUp_progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new SignInFragment());
            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
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
        email.addTextChangedListener(new TextWatcher() {
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
        password.addTextChangedListener(new TextWatcher() {
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
        confirmPassword.addTextChangedListener(new TextWatcher() {
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

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckEmailAndPassWordValidity();

            }
        });
    }
    private void checkInputs() {
        if(!TextUtils.isEmpty(fullname.getText())){
            if(!TextUtils.isEmpty(email.getText())) {
                if(!TextUtils.isEmpty(password.getText()) && password.length() >= 8) {
                    if(!TextUtils.isEmpty(confirmPassword.getText())) {
                        signupButton.setEnabled(true);
                        signupButton.setTextColor(Color.rgb(255,255,255));
                    }
                    else {
                        signupButton.setEnabled(false);
                        signupButton.setTextColor(Color.argb(50,255,255,255));
                    }
                }
                else {
                    signupButton.setEnabled(false);
                    signupButton.setTextColor(Color.argb(50,255,255,255));
                }
            }
            else {
                signupButton.setEnabled(false);
                signupButton.setTextColor(Color.argb(50,255,255,255));
            }
        }
        else{
            signupButton.setEnabled(false);
            signupButton.setTextColor(Color.argb(50,255,255,255));

        }
    }
    private void CheckEmailAndPassWordValidity() {
        Drawable errorIcon = getResources().getDrawable(R.drawable.error_icon);
        errorIcon.setBounds(0 , 0 , errorIcon.getIntrinsicWidth(),errorIcon.getIntrinsicHeight());
        if(email.getText().toString().matches(emailPattern)) {
            if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                progressBar.setVisibility(View.VISIBLE);
                signupButton.setEnabled(false);
                signupButton.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<Object, String> username = new HashMap<>();
                                    username.put("fullname", fullname.getText().toString());
                                    firebaseFirestore.collection("Users").add(username)
                                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                       mainIntent();

                                                    } else {
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        signupButton.setEnabled(true);
                                                        signupButton.setTextColor(Color.rgb(255, 255, 255));
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    signupButton.setEnabled(true);
                                    signupButton.setTextColor(Color.rgb(255, 255, 255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SignUpFragment.this.getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
            else {
                confirmPassword.setError("Password doesn't matched. Please Enter again",errorIcon);
                password.setError("Password doesn't matched. Please Enter again",errorIcon);
            }
        }
        else {
            email.setError("Email pattern Wrong",errorIcon);
        }
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private void mainIntent() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}