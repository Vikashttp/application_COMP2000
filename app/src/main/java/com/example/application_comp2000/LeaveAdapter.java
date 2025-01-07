package com.example.application_comp2000;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LeaveAdapter extends BaseAdapter {
    private final DatabaseHelper databaseHelper;
    private final Context context;
    private final List<LeaveRequest> aLeaveRequestList;

    public aPtoAdapter(Context context, List<LeaveRequest> aLeaveRequestList) {
        this.context = context;
        this.aLeaveRequestList = aLeaveRequestList;
        this.databaseHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public int getCount() {
        return aLeaveRequestList.size();
    }

    @Override
    public Object getItem(int position) {
        return aLeaveRequestList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return aLeaveRequestList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.admin_leave_list_item, parent, false);
        }

        LeaveRequest leaveRequest = aLeaveRequestList.get(position);
        if (leaveRequest.getStatus().equals("Approved") || leaveRequest.getStatus().equals("Denied")) {
            return new View(context);
        }

        Log.d("RequesterId", Integer.toString(leaveRequest.getRequesterId()));
        Employee requester = databaseHelper.getEmployeeById(leaveRequest.getRequesterId());

        TextView requesterTextView = convertView.findViewById(R.id.apto_employee_name);
        TextView dateRangeTextView = convertView.findViewById(R.id.apto_date_range);
        TextView statusTextView = convertView.findViewById(R.id.apto_status);
        TextView commentTextView = convertView.findViewById(R.id.apto_comment);

        requesterTextView.setText(requester.getFullName());
        dateRangeTextView.setText(context.getString(R.string.pto_request_dates, leaveRequest.getStartDate(), leaveRequest.getEndDate()));
        statusTextView.setText(leaveRequest.getStatus());
        commentTextView.setText(leaveRequest.getRequestComment());

        Button approveButton = convertView.findViewById(R.id.apto_approve_button);
        Button denyButton = convertView.findViewById(R.id.apto_deny_button);

        if (leaveRequest.getStatus().equals("Approved") || leaveRequest.getStatus().equals("Denied")) {
            approveButton.setVisibility(View.GONE);
            denyButton.setVisibility(View.GONE);
        }

        approveButton.setOnClickListener(v -> {
            updateRequestStatus(leaveRequest, "Approved");
        });

        denyButton.setOnClickListener(v -> {
            updateRequestStatus(leaveRequest, "Denied");
        });

        return convertView;
    }


    private void updateRequestStatus(LeaveRequest leaveRequest, String updatedStatus) {
        ContentValues statusValue = new ContentValues();
        statusValue.put("status", updatedStatus);

        leaveRequest.setStatus(updatedStatus);
        try (SQLiteDatabase db = databaseHelper.getWritableDatabase()) {
            db.update("PtoRequest", statusValue, "id = ?", new String[]{ Integer.toString(leaveRequest.getId()) });
            Toast.makeText(context, updatedStatus + " PTO Request.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Failed to update PTO Request.", Toast.LENGTH_SHORT).show();
            Log.e("UpdateRequestStatus", "Error while updating PTO request status: ", e);
        }

        NotificationHelper.showNotification(context, "PTO Status Change", "The status of your PTO Request has changed.");
        notifyDataSetChanged();
    }
}
