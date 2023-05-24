package com.example.ejob.ui.employer.applications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
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
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.ui.employer.EmployerActivity;
import com.example.ejob.ui.user.UserActivity;
import com.example.ejob.ui.user.application.JobApplication;
import com.example.ejob.utils.MyCredentials;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationItemViewHolder> {

    public List<JobApplication> mApplicationList;
    private ApplicationAdapter.ItemClickListener itemClickListener;
    JobApplication jobApplication;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userapplicationref;
    private StorageReference storage;
    View v;
    JobApplication subItem;
    String jobId;

    public ApplicationAdapter(List<JobApplication> appList, ApplicationAdapter.ItemClickListener itemClickListener1) {
        this.mApplicationList = appList;
        this.itemClickListener = itemClickListener1;
    }

    @NonNull
    @Override
    public ApplicationAdapter.ApplicationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.application_item, parent, false);
        this.v = view;
        return new ApplicationAdapter.ApplicationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ApplicationItemViewHolder holder, int position) {
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        jobApplication = mApplicationList.get(position);
        userapplicationref = firebaseDatabase.getReference("userapplications");

        // storage = firebaseDatabase.
        if (jobApplication == null) {
            return;
        }

        holder.applicantName.setText(jobApplication.getApplicantFullname());
        holder.address.setText(jobApplication.getApplicantAddress());
        holder.school.setText(jobApplication.getApplicantUniversity());
        holder.cv.setText(jobApplication.getCvitaeLink());
        holder.email.setText(jobApplication.getApplicantEmail());
        holder.phone.setText(jobApplication.getApplicantPhone());
        holder.des.setText(jobApplication.getSelfDescription());
        holder.socialmedia.setText(jobApplication.getApplicantSocialmedia());
        holder.status.setText(String.valueOf(jobApplication.getApplicationStatus()));
        holder.position.setText(jobApplication.getPosition());

        if (jobApplication.getApplicationStatus().toString().equals("SHORTLISTED")) {
            holder.add.setEnabled(false);
            holder.add.setVisibility(View.INVISIBLE);
        }

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Application Alert");
                builder.setMessage(
                        "Bạn đang duyệt đơn ứng tuyển này và ứng viên sẽ nhận được một thông báo. Bạn có chắc mình muốn tiếp tục?");
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                shortList(jobApplication.getApplicationId());
                                sendEmailNotification(jobApplication);
                                sendEmailNotificationthroughIntent(jobApplication);
                                Toast.makeText(v.getContext(), "Ứng viên được duyệt!", Toast.LENGTH_LONG).show();
                                v.getContext().startActivity(new Intent(v.getContext(), EmployerActivity.class));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
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

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPdf();
            }
        });

    }

    private void sendEmailNotification(JobApplication application) {

        String title = "Một nhà tuyển dụng đã duyệt hồ sơ của bạn!";
        String body = "Nhà tuyển dụng " + application.getApplicantFullname()
                + " đã chấp nhận đơn ứng tuyển của bạn cho công việc "
                + application.getPosition() + ".";
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "507");
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(MyCredentials.usn, MyCredentials.pwd);
                    }
                });

        try {
            // )
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MyCredentials.usn));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(application.getApplicantEmail()));
            message.setSubject(title);
            message.setText(body);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
            Log.d("email_status", thread.getState().toString());
            Toast.makeText(v.getContext(), "Một email thông báo tới ứng viên đã được gửi thành công!",
                    Toast.LENGTH_LONG).show();
        } catch (MessagingException ms) {
            throw new RuntimeException(ms);
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void sendEmailNotificationthroughIntent(JobApplication application) {

        String title = "Một nhà tuyển dụng đã duyệt hồ sơ của bạn!";
        String body = "Nhà tuyển dụng " + application.getApplicantFullname()
                + " đã chấp nhận đơn ứng tuyển của bạn cho công việc "
                + application.getPosition() + ".";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + application.getApplicantEmail()));
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        v.getContext().startActivity(intent);
        Log.d("email_intent", "here");

    }

    private void shortList(String applicationID) {

        firestore.collection("Applications")
                .document(applicationID)
                .update("applicationStatus", String.valueOf(ApplicationStatus.SHORTLISTED))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(v.getContext(), "Ứng viên được chọn thành công!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void viewPdf() {
        Intent intent = new Intent(v.getContext(), ViewPdf.class);
        intent.putExtra("cvUrl", jobApplication.getCvitaeLink());
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {

        if (mApplicationList != null) {
            return mApplicationList.size();
        }
        return 0;
    }

    public interface ItemClickListener {
        void onItemClick(JobApplication application);
    }

    public class ApplicationItemViewHolder extends RecyclerView.ViewHolder {
        TextView applicantName, school, phone, address, email, cv, socialmedia, des, status, position;
        Button button;
        ImageView photo, add;

        public ApplicationItemViewHolder(@NonNull View itemView) {
            super(itemView);
            applicantName = itemView.findViewById(R.id.applicantName);
            school = itemView.findViewById(R.id.lamp);
            phone = itemView.findViewById(R.id.apPhone);
            address = itemView.findViewById(R.id.address);
            email = itemView.findViewById(R.id.apEmail);
            cv = itemView.findViewById(R.id.cvView);
            socialmedia = itemView.findViewById(R.id.apSocial);
            des = itemView.findViewById(R.id.apSelfdescription);
            photo = itemView.findViewById(R.id.photoPreview);
            add = itemView.findViewById(R.id.changeStatus);
            status = itemView.findViewById(R.id.applicationStatus);
            position = itemView.findViewById(R.id.applicationName5);

        }
    }
}
