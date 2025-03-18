package com.example.mobilproje;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Ekranı yüklemeden önce giriş kontrolü yapma!

        // UI bileşenlerini başlat
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        mAuth = FirebaseAuth.getInstance();

        // Butonların onClickListener'larını burada tanımla
        loginButton.setOnClickListener(v -> signInUser(emailEditText.getText().toString(), passwordEditText.getText().toString()));
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Kullanıcı giriş yapmışsa doğrudan MainActivity'ye yönlendir
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            goToMainActivity();
        }
    }

    private void signInUser(String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            // Kullanıcı doğrulandıysa girişe izin ver
                            goToMainActivity();

                            Toast.makeText(getApplicationContext(),
                                    "Giriş başarılı!", Toast.LENGTH_LONG).show();
                            // Ana sayfaya yönlendirme işlemini burada yapabilirsin.
                        } else {
                            // Email doğrulanmamışsa çıkış yap ve uyarı ver
                            auth.signOut();
                            Toast.makeText(getApplicationContext(),
                                    "Lütfen e-posta adresinizi doğrulayın ve tekrar giriş yapın.",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Giriş başarısız: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void goToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Login ekranını kapat
    }
}
