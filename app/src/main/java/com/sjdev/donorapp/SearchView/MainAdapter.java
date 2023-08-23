package com.sjdev.donorapp.SearchView;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.sjdev.donorapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel, MainAdapter.myViewHolder> {

    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MainModel model) {
        holder.fullName.setText(model.getFullName());
        holder.dob.setText(model.getDob());
        holder.mobile.setText(model.getMobile());
        holder.bloodGroup.setText(model.getBloodGroup());
        holder.gender.setText(model.getGender());

        int age = getAge(model.getDob());
        holder.age.setText(String.valueOf(age));

        holder.btnDial.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getMobile()));
            holder.itemView.getContext().startActivity(dialIntent);
        });

        holder.btnShare.setOnClickListener(v -> {
            String text = "Full Name: " + model.getFullName() + "\n" +
                    "Mobile Number: " + model.getMobile() + "\n" +
                    "DOB: " + model.getDob() + "\n" +
                    "Gender: " + model.getGender() + "\n" +
                    "Blood group: " + model.getBloodGroup();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Personal Information");
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            holder.itemView.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
    }

    private int getAge(String dob) {
        int age = 0;
        try {
            Calendar dobCalendar = Calendar.getInstance();
            Calendar currentCalendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dobCalendar.setTime(Objects.requireNonNull(sdf.parse(dob)));
            int yearDiff = currentCalendar.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
            int monthDiff = currentCalendar.get(Calendar.MONTH) - dobCalendar.get(Calendar.MONTH);
            int dayDiff = currentCalendar.get(Calendar.DAY_OF_MONTH) - dobCalendar.get(Calendar.DAY_OF_MONTH);
            if (monthDiff < 0 || (monthDiff == 0 && dayDiff < 0)) {
                age = yearDiff - 1;
            } else {
                age = yearDiff;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        TextView fullName, dob, mobile, bloodGroup, gender, age;

        ImageButton btnDial, btnShare;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.searchFullName);
            dob = itemView.findViewById(R.id.searchDob);
            age = itemView.findViewById(R.id.searchAge);
            mobile = itemView.findViewById(R.id.searchMobileNo);
            bloodGroup = itemView.findViewById(R.id.searchBloodGrp);
            gender = itemView.findViewById(R.id.searchGender);

            btnDial = itemView.findViewById(R.id.searchDial);
            btnShare = itemView.findViewById(R.id.searchShare);

        }
    }
}
