package com.example.ejob.ui.user.pdf;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejob.R;
import com.example.ejob.ui.user.pdf.pdfModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

public class UploadPdf extends AppCompatActivity {

    Uri filePath;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private FloatingActionButton browse;
    private TextView fileTitle;
    private EditText cvName;
    private ImageView pdfImage;
    private WebView webView;



    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // There are no request codes
                        if (result != null) {
                            Intent data = result.getData();
                            Uri selectedFileURI = data.getData();
                            uploadProcess(selectedFileURI);
                        }
                    }
                }
            });

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_upload_pdf);

            browse = findViewById(R.id.btnBrowse);

            fileTitle = findViewById(R.id.tvFileTitle);
            pdfImage = findViewById(R.id.imgPdf);
            cvName = findViewById(R.id.etCvname);

            pdfImage.setVisibility(View.INVISIBLE);
            fileTitle.setVisibility(View.INVISIBLE);

            firebaseAuth = firebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            storageReference = FirebaseStorage.getInstance().getReference();
            databaseReference = FirebaseDatabase.getInstance().getReference("pdfFiles");


            browse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openSomeActivityForResult();
                }
            });

            pdfImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

    private void uploadProcess(Uri filePath) {
        Context context;
        ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Uploading file.....");

        final StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".pdf");
        reference.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                pdfModel pdfModel = new pdfModel(cvName.getText().toString(), uri.toString(), firebaseUser.getUid());

                                databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey())).setValue(pdfModel);
                                Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();
                                pdfImage.setVisibility(View.VISIBLE);
                                cvName.setVisibility(View.GONE);
                                fileTitle.setText(cvName.getText().toString());
                                fileTitle.setVisibility(View.VISIBLE);

                            }
                        });

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) percent + "%");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

        public void openSomeActivityForResult() {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            someActivityResultLauncher.launch(intent);
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();

        }
}