package com.example.ejob.ui.admin.user_accounts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.EmployerViewHolder> {

    public List<UserModel> mUserList;
    public Context context;
    Boolean testClick = false;
    private ItemClickListener itemClickListener;
    private DatabaseReference appliedReference;
    FirebaseFirestore fStore;
    FirebaseDatabase fBase;

    public UserAdapter(List<UserModel> mEmployerList, ItemClickListener itemClickListener1) {
        this.mUserList = mEmployerList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public EmployerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usr_account_item, parent, false);
        return new EmployerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployerViewHolder holder, int position) {
        UserModel user = mUserList.get(position);
        if (user == null) {
            return;
        }
        fStore = FirebaseFirestore.getInstance();
        fBase = FirebaseDatabase.getInstance();

        Date datecurrent = Date.getInstance();

        // holder.employerAvatar.setImageResource(jobPosting.getImageUrl());
        holder.employerName.setText(user.getUserFullname());
        holder.empLocation.setText(user.getUsrAddress());

        if (user.getUserStatus().equals("false")) {
            holder.accountStatus.setText("Bị khoá");
            holder.accountStatus.setBackgroundResource(R.drawable.bg_account_type_blocked);
        } else {
            holder.accountStatus.setText("Đang hoạt động");
            holder.accountStatus.setBackgroundResource(R.drawable.bg_account_type_available);

        }

        long dateCurrent = datecurrent.getEpochSecond();
        Date datePosted = Date.getInstance(Long.parseLong(user.getDateCreated()));
        long daysDiffInEpoch;
        try {
            Long dateCreated = Long.parseLong(user.getDateCreated());
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
        holder.unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.empLocation.setText(user.getUsrAddress());
        holder.school.setText(user.getUsrEmail());

        holder.unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Block alert");
                builder.setMessage("Bạn đang thay đổi quyền truy cập của tài khoản " + user.getUserFullname()
                        + " này. \nBạn chắc rằng mình muốn tiếp tục?");
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (user.getUserStatus().equals("true")) {
                                    lockEvent(user);
                                } else {
                                    unlockEvent(user);
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

    private void unlockEvent(UserModel model) {
        fStore.collection("Users")
                .document(model.getUserId())
                .update("isAvailable", true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log.d("fstr_blocked", aVoid.toString());
                        Toast.makeText(context, "Đã mở khoá!", Toast.LENGTH_LONG).show();
                    }
                });

        fBase.getReference("Blocked")
                .child(model.getUserId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Log.d("fb_blocked", task.getResult().toString());
                    }
                });
    }

    private void lockEvent(UserModel model) {

        fStore.collection("Users")
                .document(model.getUserId())
                .update("isAvailable", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log.d("fstr_blocked", aVoid.toString());
                        Toast.makeText(context, "Đã khoá!", Toast.LENGTH_LONG).show();
                    }
                });

        fBase.getReference("Blocked")
                .child(model.getUserId())
                .setValue(true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        if (mUserList != null) {
            return mUserList.size();
        }
        return 0;
    }

    public interface ItemClickListener {
        void onItemClick(Employer employer);
    }

    public class EmployerViewHolder extends RecyclerView.ViewHolder {

        private ImageView employerAvatar, unlock;
        private TextView jobPosition, employerName, empLocation, tvDaysago, jobsNumber, accountStatus, school;

        public EmployerViewHolder(@NonNull View itemView) {
            super(itemView);

            mapping();

        }

        private void mapping() {
            unlock = itemView.findViewById(R.id.lockIcon);
            employerName = itemView.findViewById(R.id.tvEmpName);
            empLocation = itemView.findViewById(R.id.address);
            school = itemView.findViewById(R.id.tvIndustry);
            tvDaysago = itemView.findViewById(R.id.accountCreationDate);
            accountStatus = itemView.findViewById(R.id.accStatus);
        }

    }

}
