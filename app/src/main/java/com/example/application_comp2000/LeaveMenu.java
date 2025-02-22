package com.example.application_comp2000;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.TextView;

public class LeaveMenu extends AppCompatActivity {

    com.example.employeeapp.Employee currentUser;
    com.example.employeeapp.DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leave_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = com.example.employeeapp.DatabaseHelper.getInstance(this);
        currentUser = databaseHelper.loadCurrentUser(this);

        TextView employeeNameTextView = findViewById(R.id.employeeName);
        employeeNameTextView.setText(currentUser.getFullName());
    }

    public void launchRequestPto(View v)
    {
        Intent iLaunchRequestPto = new Intent(this, Requestleave.class);
        startActivity(iLaunchRequestPto);
    }

    public void launchViewHoliday(View v)
    {
        Intent iLaunchViewHoliday = new Intent(this, ViewHoliday.class);
        startActivity(iLaunchViewHoliday);
    }
}