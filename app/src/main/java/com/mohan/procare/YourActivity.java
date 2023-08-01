package com.mohan.procare;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class YourActivity extends AppCompatActivity {

    // ...

    @Override
    public void onBackPressed() {
        // Check if the user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User is logged in, allow the back button to work normally
            super.onBackPressed();
        } else {
            // User is logged out, prevent going back to the previous page
            // Or you can show a toast message indicating that the user is logged out
        }
    }
}
