package com.example.application_comp2000;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ListView;
import java.util.List;

import android.util.Log;

public class ViewHoliday extends AppCompatActivity {

    com.example.employeeapp.DatabaseHelper databaseHelper;
    com.example.employeeapp.Employee currentUser;
    com.example.employeeapp.PtoAdapter ptoAdapter;
    ListView ptoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_holiday);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = com.example.employeeapp.DatabaseHelper.getInstance(this);
        currentUser = databaseHelper.loadCurrentUser(this);

        ptoListView = findViewById(R.id.ptoListView);
        List<com.example.employeeapp.PtoRequest> ptoRequestList = databaseHelper.getAllPtoRequestsByRequester(currentUser.getId());
        ptoAdapter = new com.example.employeeapp.PtoAdapter(this, ptoRequestList);
        ptoListView.setAdapter(ptoAdapter);
    }
}
