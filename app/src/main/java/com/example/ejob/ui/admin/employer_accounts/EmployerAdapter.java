package com.example.ejob.ui.admin.employer_accounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ejob.R;
import com.example.ejob.data.model.EmployerModel;
import com.example.ejob.ui.user.JobApplying;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.ui.user.userjob.AllJobAdapter;
import com.example.ejob.utils.Date;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EmployerAdapter extends RecyclerView.Adapter<EmployerAdapter.EmployerViewHolder> {

    public List<EmployerModel> mEmployerList;
    public Context context;
    Boolean testClick = false;
    private ItemClickListener itemClickListener;
    private DatabaseReference lockRef;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fDatabase;
    private FirebaseFirestore fStore;

    public EmployerAdapter(List<EmployerModel> mEmployerList, ItemClickListener itemClickListener1) {
        this.mEmployerList = mEmployerList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public EmployerAdapter.EmployerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employer_account_item, parent, false);
        context = parent.getContext();
        return new EmployerAdapter.EmployerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployerAdapter.EmployerViewHolder holder, int position) {
        EmployerModel employer = mEmployerList.get(position);
        if (employer == null) {
            return;
        }

        init();
        // holder.employerAvatar.setImageResource(jobPosting.getImageUrl());
        holder.employerName.setText(employer.getEmployerFullname());
        holder.empLocation.setText(employer.getEmployerAddress());
        holder.industry.setText(employer.getEmployerIndustry());
        Date datecurrent = Date.getInstance();

        if (employer.getStatus().equals("false")) {
            holder.accountStatus.setText("Bị khoá");
            holder.accountStatus.setBackgroundResource(R.drawable.bg_account_type_blocked);
        } else {
            holder.accountStatus.setText("Đang hoạt động");
            holder.accountStatus.setBackgroundResource(R.drawable.bg_account_type_available);

        }
        long dateCurrent = datecurrent.getEpochSecond();
        Date datePosted = Date.getInstance(Long.parseLong(employer.getDateCreationEmployer()));
        long daysDiffInEpoch;

        Date dateDiff;
        try {
            Long dateCreated = Long.parseLong(employer.getDateCreationEmployer());
            String dateShown = datePosted.toString();
            Log.d("TAG1", String.valueOf(dateCurrent));
            Log.d("TAG2", dateShown);
            daysDiffInEpoch = dateCurrent - dateCreated;
            int daysTogo = (int) daysDiffInEpoch / 86400;
            Log.d("TAG3", String.valueOf(daysDiffInEpoch));
            if (daysTogo < 1) {
                holder.tvDaysago.setText("Mới tạo hôm nay");
            } else if (daysTogo < 2) {
                holder.tvDaysago.setText("Được tạo " + daysTogo + " ngày trước");
            } else {
                holder.tvDaysago.setText("Được tạo " + daysTogo + " ngày trước");
            }
        } catch (NumberFormatException e) {
            Log.d("NBE", e.getMessage());
        }

        // holder.getLockStatus(employer.getEmployerId());

        holder.changeAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Block alert");
                builder.setMessage("Bạn đang thay đổi quyền truy cập của tài khoản " + employer.getEmployerFullname()
                        + " này. \nBạn chắc rằng mình muốn tiếp tục?");
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (employer.getStatus().equals("true")) {
                                    lockEvent(employer);
                                } else {
                                    unlockEvent(employer);
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                builder.setPositiveButton("Yes", dialogListener);
                builder.setNegativeButton("No", dialogListener);
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

    }

    private void unlockEvent(EmployerModel employerModel) {
        fStore.collection("Users")
                .document(employerModel.getEmployerId())
                .update("isAvailable", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log.d("fstr_blocked", aVoid.toString());
                        Toast.makeText(context, "Đã mở khoá!", Toast.LENGTH_LONG).show();
                    }
                });

        fDatabase.getReference("Blocked")
                .child(employerModel.getEmployerId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Log.d("fb_blocked", task.getResult().toString());
                    }
                });
    }

    private void lockEvent(EmployerModel employerModel) {

        fStore.collection("Users")
                .document(employerModel.getEmployerId())
                .update("isAvailable", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log.d("fstr_blocked", aVoid.toString());
                        Toast.makeText(context, "Đã khoá!", Toast.LENGTH_LONG).show();
                    }
                });

        fDatabase.getReference("Blocked")
                .child(employerModel.getEmployerId())
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Log.d("fb_blocked", task.getResult().toString());
                    }
                });

    }

    private void init() {
        fAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        fStore = FirebaseFirestore.getInstance();
        lockRef = fDatabase.getReference("Blocked");
    }

    @Override
    public int getItemCount() {
        if (mEmployerList != null) {
            return mEmployerList.size();
        }
        return 0;
    }

    public interface ItemClickListener {
        void onItemClick(Employer employer);
    }

    public class EmployerViewHolder extends RecyclerView.ViewHolder {

        private ImageView employerAvatar, changeAccess;
        private TextView jobPosition, employerName, empLocation, tvDaysago, jobsNumber, accountStatus, industry;

        public EmployerViewHolder(@NonNull View itemView) {
            super(itemView);

            mapping();
        }

        private void getLockStatus(String uid) {
            lockRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(uid).exists()) {
                        changeAccess.setImageResource(R.drawable.ic_baseline_lock_24);
                        accountStatus.setText("Bị khoá");
                        accountStatus.setBackgroundResource(R.drawable.bg_account_type_blocked);
                    } else {
                        changeAccess.setImageResource(R.drawable.ic_baseline_lock_open_24);
                        accountStatus.setText("Đang hoạt động");
                        accountStatus.setBackgroundResource(R.drawable.bg_account_type_available);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void mapping() {
            changeAccess = itemView.findViewById(R.id.lockIcon);
            employerName = itemView.findViewById(R.id.tvEmpName);
            empLocation = itemView.findViewById(R.id.address);
            tvDaysago = itemView.findViewById(R.id.accountCreationDate);
            accountStatus = itemView.findViewById(R.id.accStatus);
            industry = itemView.findViewById(R.id.tvIndustry);
        }

    }

}
