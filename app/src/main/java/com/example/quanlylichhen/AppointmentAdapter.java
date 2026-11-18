package com.example.quanlylichhen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {
    public AppointmentAdapter(Context context, List<Appointment> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_appointment, parent, false);
        Appointment a = getItem(position);
        ((TextView) convertView.findViewById(R.id.tvTitle)).setText(a.getTitle());
        ((TextView) convertView.findViewById(R.id.tvLocation)).setText(a.getLocation());
        ((TextView) convertView.findViewById(R.id.tvDateTime)).setText(a.getDisplayDateTime());

        TextView tvReminder = convertView.findViewById(R.id.tvReminder);
        tvReminder.setText(a.isReminder() ? "Có nhắc" : "Không nhắc");
        tvReminder.setTextColor(ContextCompat.getColor(getContext(),
                a.isReminder() ? R.color.ptit_red : R.color.ptit_text_sub));
        return convertView;
    }
}
