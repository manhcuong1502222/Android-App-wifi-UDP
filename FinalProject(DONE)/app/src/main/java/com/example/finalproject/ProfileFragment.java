package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// ... (imports remain unchanged)

public class ProfileFragment extends Fragment {
    private View mView;
    private EditText usernameTextView, emailTextView, genderTextView, phoneTextView;
    private TextView txtProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize EditText fields
        usernameTextView = mView.findViewById(R.id.username);
        emailTextView = mView.findViewById(R.id.email);
        genderTextView = mView.findViewById(R.id.gender);
        phoneTextView = mView.findViewById(R.id.phone);
        txtProfile = mView.findViewById(R.id.txtProfile);

        Button saveButton = mView.findViewById(R.id.btnsave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });

        // Load existing data from SharedPreferences when the fragment is created
        loadProfileData();

        return mView;
    }

    // Method to load existing user profile data from SharedPreferences
    private void loadProfileData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        usernameTextView.setText(sharedPreferences.getString("username", ""));
        emailTextView.setText(sharedPreferences.getString("email", ""));
        genderTextView.setText(sharedPreferences.getString("gender", ""));
        phoneTextView.setText(sharedPreferences.getString("phone", ""));
        txtProfile.setText(sharedPreferences.getString("username", ""));
    }

    // Method to handle save button click
    public void saveProfileChanges() {
        // Get the updated information from EditText fields
        String newUsername = usernameTextView.getText().toString();
        String newEmail = emailTextView.getText().toString();
        String newGender = genderTextView.getText().toString();
        String newPhone = phoneTextView.getText().toString();

        // Save the updated information to SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", newUsername);
        editor.putString("email", newEmail);
        editor.putString("gender", newGender);
        editor.putString("phone", newPhone);
        editor.apply();

        // Optional: Show a message or perform any other actions after saving changes
        Toast.makeText(getActivity(), "Changes saved successfully", Toast.LENGTH_SHORT).show();
    }
}
