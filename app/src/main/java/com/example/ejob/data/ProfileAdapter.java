package com.example.ejob.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.utils.Date;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    public ApplicantModel model;
    private ItemClickListener itemClickListener;

    public Context context;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference likeReference;
    private DatabaseReference appliedReference;
    Boolean testClick = false;
    private ShimmerFrameLayout shimmerFrameLayout;

    public ProfileAdapter(ApplicantModel mModel) {
        this.model = mModel;
    }

    @NonNull
    @Override
    public ProfileAdapter.ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_profile, parent, false);
        return new ProfileAdapter.ProfileViewHolder(view);
    }

    public interface ItemClickListener {
        void onItemClick(ApplicantModel applicantModel);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ProfileViewHolder holder, int position) {
        firebaseAuth = firebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();

        if (model == null) {
            return;
        }

        holder.fullname.setText(model.getApplicantFullname());
        holder.email.setText(model.getApplicantEmail());
        holder.address.setText(model.getApplicantAddress());
        holder.school.setText(model.getApplicantUniversity());
        holder.dob.setText("30/12/1994");
        holder.phone.setText(model.getApplicantPhone());
        holder.address.setText(model.getApplicantAddress());

        holder.avtar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {

        private ImageView avtar;
        private EditText email, fullname, dob, school, phone, address;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            avtar = itemView.findViewById(R.id.userAvatar);
            email = itemView.findViewById(R.id.etEmailUP1);
            fullname = itemView.findViewById(R.id.etFullnameUP1);
            dob = itemView.findViewById(R.id.etDateofBirthUP1);
            school = itemView.findViewById(R.id.etSchoolUP1);
            phone = itemView.findViewById(R.id.etPersonalPhoneUP1);
            address = itemView.findViewById(R.id.etPersonalAddressUP1);
        }
    }
}
