package com.example.application_comp2000;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EmployeeAdapter extends BaseAdapter {
    private static EmployeeAdapter instance;
    private final com.example.employeeapp.DatabaseHelper databaseHelper;
    private final Context context;
    private final List<com.example.employeeapp.Employee> employeeList;

    public EmployeeAdapter(Context context, List<com.example.employeeapp.Employee> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
        this.databaseHelper = com.example.employeeapp.DatabaseHelper.getInstance(context);
    }

    @Override
    public int getCount() {
        return employeeList.size();
    }

    @Override
    public Object getItem(int position) {
        return employeeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return employeeList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.employee_list_item, parent, false);
        }

        com.example.employeeapp.Employee employee = employeeList.get(position);

        String salaryString = context.getString(R.string.salary_string, employee.getSalary());
        String additionalInfo = context.getString(R.string.additional_info, salaryString, employee.getStartDate(), employee.getId());

        TextView fullNameTextView = convertView.findViewById(R.id.listItemEmployeeFullName);
        TextView emailTextView = convertView.findViewById(R.id.listItemEmployeeEmail);
        TextView departmentTextView = convertView.findViewById(R.id.listItemEmployeeDepartment);
        TextView additionalInfoTextView = convertView.findViewById(R.id.listItemAdditionalInfo);

        Button deleteButton = convertView.findViewById(R.id.delete_employee_button);
        Button editButton = convertView.findViewById(R.id.edit_employee_button);

        fullNameTextView.setText(employee.getFullName());
        emailTextView.setText(employee.getEmail());
        departmentTextView.setText(employee.getDepartment());
        additionalInfoTextView.setText(additionalInfo);

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder confirmDeletion = new AlertDialog.Builder(context);
            confirmDeletion.setMessage("Confirm deletion?");
            confirmDeletion.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        com.example.employeeapp.ApiService.apiDeleteUser(context, employee.getId());
                    } catch (Exception e) {
                        Toast.makeText(context, "Failed to delete Employee from API", Toast.LENGTH_SHORT).show();
                    }
                    try {
                        databaseHelper.deleteEmployee(employee.getId());
                    } catch (Exception e) {
                        Toast.makeText(context, "Failed to delete Employee Locally", Toast.LENGTH_SHORT).show();
                    }
                    employeeList.remove(position);
                    notifyDataSetChanged(); // Refreshes ListView
                    Toast.makeText(context, "Deleted Employee", Toast.LENGTH_SHORT).show();
                }
            });
            confirmDeletion.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            confirmDeletion.show();
        });

        editButton.setOnClickListener(v -> {
            Intent iLaunchEditEmployee = new Intent(context, com.example.employeeapp.aEditEmployee.class);
            iLaunchEditEmployee.putExtra("employeeId", employee.getId());
            context.startActivity(iLaunchEditEmployee);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
