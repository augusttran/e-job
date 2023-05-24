package com.example.ejob.ui.user.applications;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicationStatus;
import com.example.ejob.ui.employer.AddJob;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.employer.ViewJobDetail2;
import com.example.ejob.ui.user.UserActivity;
import com.example.ejob.ui.user.application.JobApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ApplicationAdapterforUser extends RecyclerView.Adapter<ApplicationAdapterforUser.ApplicationItemViewHolder> {

    public List<JobApplication> mApplicationList;
    private ApplicationAdapterforUser.ItemClickListener itemClickListener;
    JobApplication jobApplication2;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userapplicationref;
    private StorageReference storage;
    private FirebaseUser firebaseUser;
    View v;
    String jobId;
    boolean cancelled = false;
    Notification notificationManager;


    public ApplicationAdapterforUser(List<JobApplication> appList, ApplicationAdapterforUser.ItemClickListener itemClickListener1) {
        this.mApplicationList = appList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public ApplicationAdapterforUser.ApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item_2, parent, false);
        this.v = view;
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userapplicationref = firebaseDatabase.getReference("userapplications");

        return new ApplicationAdapterforUser.ApplicationItemViewHolder(view);
    }

    @Override
    public void onViewRecycled(@NonNull ApplicationItemViewHolder holder) {
        super.onViewRecycled(holder);

    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapterforUser.ApplicationItemViewHolder holder, int position) {
        JobApplication jobApplication = mApplicationList.get(position);
        jobApplication2 = mApplicationList.get(position);

        if(jobApplication == null){
            return;
        }


        if(jobApplication2.getApplicationStatus().equals("CANCELLED")){
            holder.status.setText("Đã huỷ");
            holder.status.setBackgroundResource(R.drawable.application_status_cancelled);
        }else if(jobApplication2.getApplicationStatus().equals("SUBMITTED")){
            holder.status.setText("Đã nộp đơn");
            holder.status.setBackgroundResource(R.drawable.application_status_submitted);
        }else{
            holder.status.setText("Đã được duyệt");
            holder.status.setBackgroundResource(R.drawable.application_status_shortlisted);
        }

        holder.applicantName.setText(jobApplication.getPosition());
        holder.ngaytao.setText(jobApplication.getApplicationDate());
        holder.madon.setText(jobApplication.getApplicationId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewApplicationDetail.class);
                intent.putExtra("myapplication",jobApplication);
                v.getContext().startActivity(intent);
            }
        });

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewApplicationDetail.class);
                intent.putExtra("myapplication",jobApplication);
                v.getContext().startActivity(intent);
            }
        });
    }



    private void doNothing() {
        Toast.makeText(v.getContext(), "Không thể huỷ", Toast.LENGTH_SHORT).show();
    }


    private void reduceAppliedNumber() {



    }

    private void cancelApplication(JobApplication jobApplication) {

        Runnable runnable = () -> {
            jobApplication.setApplicationStatus("CANCELLED");
            firestore.collection("Applications")
                    .document(jobApplication.getApplicationId())
                    .update("applicationStatus", String.valueOf(ApplicationStatus.CANCELLED))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(v.getContext(), "Huỷ đơn thành công!", Toast.LENGTH_SHORT).show();
                            cancelled = true;
                        }
                    });

            userapplicationref
                    .child(jobApplication.getJobID())
                    .child(firebaseAuth.getCurrentUser().getUid())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    });
        };
        Thread thread = new Thread(runnable);

        thread.start();


    }




    private void viewPdf() {
        Intent intent = new Intent(v.getContext(), ViewPdf.class);
        intent.putExtra("cvUrl", jobApplication2.getCvitaeLink());
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {

        if(mApplicationList != null) {
            return mApplicationList.size();
        }
        return 0;
    }

    public interface ItemClickListener{
        void onItemClick(JobApplication application);
    }

    public class ApplicationItemViewHolder extends RecyclerView.ViewHolder implements com.example.ejob.ui.user.applications.ApplicationItemViewHolder {
        TextView applicantName, school, phone, address, email, cv, socialmedia, des, status, applicant, madon, ngaytao;
        ImageView img;
        Button button;
        ImageView photo, cancelBtn;


        public ApplicationItemViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.title);
            madon = itemView.findViewById(R.id.lamp);
            ngaytao = itemView.findViewById(R.id.address);
            photo = itemView.findViewById(R.id.photoPreview);
            status = itemView.findViewById(R.id.tvJobType111);

        }


    }
}
