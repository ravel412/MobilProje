package com.example.mobilproje;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button logoutButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.logoutButton);
        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Kullanıcı oturum açmış mı kontrol et
        checkUserSession();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserSession();
    }

    /**
     * Kullanıcı oturum açmış mı ve doğrulaması yapılmış mı kontrol eder.
     */
    private void checkUserSession() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            // Kullanıcı oturum açmamış, giriş ekranına yönlendir
            goToLoginActivity();
        } else {
            if (!user.isEmailVerified()) {
                // Kullanıcının e-postası doğrulanmamışsa çıkış yap ve giriş ekranına yönlendir
                mAuth.signOut();
            //    Toast.makeText(this, "Lütfen e-posta adresinizi doğrulayın.", Toast.LENGTH_LONG).show();
                goToLoginActivity();
            } else {
                // Kullanıcı giriş yapmış ve e-posta doğrulanmışsa uygulamada kalabilir
                Log.d("Auth", "Kullanıcı doğrulanmış, girişe izin verildi." + user.getEmail());
            }
        }
    }

    private void logoutUser() {
        mAuth.signOut(); // Firebase'den çıkış yap
        Toast.makeText(MainActivity.this, "Çıkış yapıldı!", Toast.LENGTH_SHORT).show();

        goToLoginActivity(); // Kullanıcıyı giriş ekranına yönlendir
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Geri tuşuna basınca tekrar MainActivity açılmasın
        startActivity(intent);
        finish(); // MainActivity'yi kapat
    }
}
