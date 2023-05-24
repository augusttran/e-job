package com.example.ejob.ui.employer.applications;

import android.content.Context;
import android.content.Intent;
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
import com.example.ejob.ui.employer.ViewJobDetail2;
import com.example.ejob.ui.user.application.ViewJobDetail;
import com.example.ejob.ui.user.userjob.AllJobAdapter;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.utils.Date;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyJobsAdapter extends RecyclerView.Adapter<MyJobsAdapter.JobInfoViewHolder> {

    public List<com.example.ejob.ui.user.userjob.JobPostingforUser> mJobList;
    private MyJobsAdapter.ItemClickListener itemClickListener;
    public Context context;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference likeReference;
    private DatabaseReference appliedReference;
    Boolean testClick = false;
    int appliedNum = 0;

    public MyJobsAdapter(List<JobPostingforUser> itemList, MyJobsAdapter.ItemClickListener clickListener) {
        this.mJobList = itemList;
        this.itemClickListener = clickListener;
    }

    @NonNull
    @Override
    public MyJobsAdapter.JobInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item_emp, parent, false);
        return new MyJobsAdapter.JobInfoViewHolder(view);
    }

    public interface ItemClickListener {
        void onItemClick(JobPostingforUser jobPost);
    }

    @Override
    public void onBindViewHolder(@NonNull MyJobsAdapter.JobInfoViewHolder holder, int position) {
        firebaseAuth = firebaseAuth.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        JobPostingforUser jobPosting = mJobList.get(position);

        if (jobPosting == null) {
            return;
        }
        holder.employerName.setText(jobPosting.getEmployerName());
        holder.jobPosition.setText(jobPosting.getJobTitle());
        holder.jobLocation.setText(jobPosting.getJobLocation());
        holder.jobtype.setText(jobPosting.getJobType());

        Date datecurrent = Date.getInstance();
        long dateCurrent = datecurrent.getEpochSecond();
        Date datePosted = Date.getInstance(Long.parseLong(jobPosting.getJobDateCreated()));
        long daysDiffInEpoch;
        Date dateDiff;
        try {
            Long dateCreated = Long.parseLong(jobPosting.getJobDateCreated());
            String dateShown = datePosted.toString();
            Log.d("TAG1", String.valueOf(dateCurrent));
            Log.d("TAG2", dateShown);
            daysDiffInEpoch = dateCurrent - dateCreated;
            int daysTogo = (int) daysDiffInEpoch / 86400;
            Log.d("TAG3", String.valueOf(daysDiffInEpoch));
            if (daysTogo < 1) {
                holder.tvDaysago.setText("Mới vừa đăng");
            } else if (daysTogo < 2) {
                holder.tvDaysago.setText("Đã đăng " + daysTogo + " ngày trước");
            } else {
                holder.tvDaysago.setText("Đã đăng " + daysTogo + " ngày trước");
            }
        } catch (NumberFormatException e) {
            Log.d("NBE", e.getMessage());
        }

        holder.getLikeStatus(holder, jobPosting.getJobId(), userID);

        holder.getAppliedNumber(jobPosting.getJobId());
        holder.unheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testClick = true;

                likeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (testClick == true) {

                            if (snapshot.child(jobPosting.getJobId()).hasChild(userID)) {
                                likeReference.child(jobPosting.getJobId()).child(userID).removeValue();
                                testClick = false;
                            } else {
                                likeReference.child(jobPosting.getJobId()).child(userID).setValue(true);
                                Toast.makeText(holder.employerName.getContext(),
                                        "You liked this " + holder.jobPosition.getText() + " job", Toast.LENGTH_LONG)
                                        .show();
                                testClick = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.employerAvatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewJobDetail2.class);
                intent.putExtra("myJobposting", jobPosting);
                intent.putExtra("appliedNumber", String.valueOf(appliedNum));
                v.getContext().startActivity(intent);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ViewJobDetail2.class);
                intent.putExtra("myJobposting", jobPosting);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        if (mJobList != null) {
            return mJobList.size();
        }
        return 0;
    }

    public class JobInfoViewHolder extends RecyclerView.ViewHolder {

        private ImageView employerAvatar, unheart;
        private TextView jobPosition, employerName, jobLocation, tvDaysago, likesNumber, appliedNumber, jobtype;
        private MaterialCardView singleCardView;

        public JobInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            unheart = itemView.findViewById(R.id.btUnheart);
            likesNumber = itemView.findViewById(R.id.tvLikes);
            employerAvatar = itemView.findViewById(R.id.photoPreview);
            jobPosition = itemView.findViewById(R.id.title);
            employerName = itemView.findViewById(R.id.lamp);
            jobLocation = itemView.findViewById(R.id.address);
            tvDaysago = itemView.findViewById(R.id.dateCreated);
            jobtype = itemView.findViewById(R.id.tvJobType111);
            appliedNumber = itemView.findViewById(R.id.tvAppliedNum);

        }

        public void getLikeStatus(MyJobsAdapter.JobInfoViewHolder holder, String postId, String uid) {
            likeReference = FirebaseDatabase.getInstance().getReference("likes");
            likeReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postId).hasChild(uid)) {
                        int likeCount = (int) snapshot.child(postId).getChildrenCount();
                        likesNumber.setText(String.valueOf(likeCount));

                        unheart.setImageResource(R.drawable.heart);
                    } else {
                        int likeCount = (int) snapshot.child(postId).getChildrenCount();
                        likesNumber.setText(String.valueOf(likeCount));

                        unheart.setImageResource(R.drawable.unheart);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void getAppliedNumber(String postId) {
            appliedReference = FirebaseDatabase.getInstance()
                    .getReference("userapplications");
            appliedReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postId).exists()) {
                        int appCount = (int) snapshot.child(postId).getChildrenCount();
                        appliedNum = appCount;
                        appliedNumber.setText(String.valueOf(appCount));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}
