package com.example.ejob.ui.register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Supplier;

import com.example.ejob.R;
import com.example.ejob.ui.login.LoginActivity;
import com.example.ejob.utils.Date;
import com.example.ejob.utils.DatePickerDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static com.squareup.picasso.Picasso.*;

public class RegisterUsr extends AppCompatActivity {
    boolean valid = true;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase fDb;
    DatabaseReference profileRef, availRef, authenRef;
    FirebaseStorage fStorage;
    private EditText fullName, email, password, phone, school, address, etDob, avatarUrl;
    private Button registerButton, loginButton;
    private ImageView emCheck, psCheck, avatar, dob, checkAvatar;
    private TextView dobtv;
    private String usEmail, usPassword, usDob, usDatecreated, usFullname, usPhone, usSchool, usAddress, imgLink;
    private Uri imgUri;
    private Context context;
    boolean uploaded;
    boolean newUser;
    Date date;

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {

                    if (result != null) {
                        avatar.setImageURI(result);
                        imgUri = result;
                    }
                }
            });

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // if(valEmail()){
            // firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString())
            // .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            // @Override
            // public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
            // boolean newUser = task.getResult().getSignInMethods().isEmpty();
            // if(!newUser){
            // email.setError("Email đã tồn tại");
            // }
            // }
            // });
            // }
            if (valEmail() && valInputPass()) {
                emCheck.setVisibility(View.VISIBLE);
                emCheck.setImageResource(R.drawable.ic_baseline_check_ok_24);
            }
            if (valAddress() && valFullName() && valPhoneNum() && valDateOfBirth()
                    && !school.getText().toString().isEmpty()) {
                psCheck.setVisibility(View.VISIBLE);
                psCheck.setImageResource(R.drawable.ic_baseline_check_ok_24);
            }
            registerButton.setEnabled(valEmail() && valAddress() && valFullName()
                    && valPhoneNum() && !school.getText().toString().isEmpty() && valDateOfBirth());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private static boolean containUpperCase(String str) {
        for (char c : str.toCharArray())
            if (Character.isUpperCase(c))
                return true;
        return false;
    }

    private static boolean containLowerCase(String str) {
        for (char c : str.toCharArray())
            if (Character.isLowerCase(c))
                return true;
        return false;
    }

    private static boolean containSpecialChar(String str) {
        String specialChars = ",./!@#$%^&*()-_+=~[]\\|{}[]";
        Supplier<Stream<String>> supplier = () -> toStream(str);
        for (char c : specialChars.toCharArray())
            if (supplier.get().anyMatch(item -> item.equals(String.valueOf(c))))
                return true;
        return false;
    }

    private static Stream<String> toStream(String text) {
        String[] res = new String[text.length()];
        for (int i = 0; i < text.length(); i++) {
            res[i] = String.valueOf(text.charAt(i));
        }
        return Arrays.stream(res);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        context = this;
        mapping();
        buttonSet(0);
        init();

        fullName.addTextChangedListener(registerTextWatcher);
        email.addTextChangedListener(registerTextWatcher);
        password.addTextChangedListener(registerTextWatcher);
        phone.addTextChangedListener(registerTextWatcher);
        school.addTextChangedListener(registerTextWatcher);
        address.addTextChangedListener(registerTextWatcher);

        etDob.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(new DatePickerDialog.OnDatePickedListener() {
                @Override
                public void onDateOk(int date, int month, int year) {
                    ((EditText) v).setError(null);
                    try {
                        ((EditText) v).setText(Date.getInstance(date, month - 1, year).toString());

                    } catch (IllegalArgumentException ae) {
                        ae.printStackTrace();
                    }
                }

                @Override
                public void onDateError(int date, int month, int year) {
                    v.requestFocus();
                    ((EditText) v).setError("Tuổi phải lớn hơn hoặc bằng 18");
                }
            }, (date, month, year) -> {
                Date dateObj = Date.getInstance();
                Date minValidDate = Date.getInstance(date, month, year + 18);
                return minValidDate.getEpochSecond() <= dateObj.getEpochSecond();
            });
            dialog.show(getSupportFragmentManager(), null);
        });
        avatar.setOnClickListener(v -> uploadImageEvent());
        registerButton.setOnClickListener(v -> registerEvent());

    }

    private void uploadImageEvent() throws IllegalStateException, NullPointerException {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Upload image");
        progressDialog.setMessage("Progress Bar");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        getContent.launch("image/*");
        if (imgUri != null) {
            int cvRandom = new Random().nextInt(5000);
            double progress = 0;
            StorageReference folder = fStorage.getInstance().getReference().child("images/");
            String possibleNameFile = imgUri.getLastPathSegment().replaceAll(".*/", "");
            StorageReference file_name = folder.child(possibleNameFile);
            HashMap<String, String> hashMap = new HashMap<>();

            folder.putFile(imgUri)
                    .addOnProgressListener(snapshot -> {
                        double progress1 = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.show();
                        progressDialog.setProgress(((int) progress1));
                        if (progress1 == 100) {
                            progressDialog.setMessage("Uploaded " + progress1 + " %");
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete())
                                ;
                            uploaded = true;
                            Uri uri = uriTask.getResult();
                            imgLink = uri.toString();
                            hashMap.put("imgURL", imgLink);
                        }
                    })
                    .addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            if (task1.isComplete()) {
                                progressDialog.setCancelable(true);
                                progressDialog.dismiss();

                                hashMap.put("imgUri", String.valueOf(task1.getResult().getStorage().getDownloadUrl()));
                                hashMap.put("fileName", possibleNameFile);
                                avatarUrl.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        avatarUrl.setText("Photo URL" + imgLink);
                                    }
                                });
                                fDb.getReference("imgUploads")
                                        .child(firebaseAuth.getCurrentUser().getUid())
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Done uploading!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {

                            }
                        }
                    });

        }

    }

    private void buttonSet(int i) {
        switch (i) {

            case 0:
                emCheck.setVisibility(View.INVISIBLE);
                psCheck.setVisibility(View.INVISIBLE);
                registerButton.setEnabled(valEmail() && valAddress() && valFullName() && valPhoneNum());
                registerButton.setBackground(getDrawable(R.drawable.button_grayout));
                break;

            case 1:
                registerButton.setEnabled(valEmail() && valAddress() && valFullName() && valPhoneNum());
                registerButton.setBackground(getDrawable(R.drawable.button_bg));
                emCheck.setImageResource(R.drawable.ic_baseline_check_ok_24);
                psCheck.setImageResource(R.drawable.ic_baseline_check_ok_24);
                break;

            case 2: // email not OK
                registerButton.setEnabled(valEmail() && valAddress() && valFullName() && valPhoneNum());
                registerButton.setBackground(getDrawable(R.drawable.button_grayout));

                psCheck.setImageResource(R.drawable.ic_baseline_check_ok_24);
                break;

            case 3: // person not OK not OK
                registerButton.setEnabled(valEmail() && valAddress() && valFullName() && valPhoneNum());
                registerButton.setBackground(getDrawable(R.drawable.button_grayout));

                emCheck.setImageResource(R.drawable.ic_baseline_check_ok_24);
                break;

        }
    }

    private void registerEvent() {
        if (valEmail() && valFullName() && valAddress() && valPhoneNum()) {
            emCheck.setVisibility(View.VISIBLE);
            emCheck.setColorFilter(R.color.green_effective);
        }
        getTextFromEditTexts();

        firebaseAuth.createUserWithEmailAndPassword(usEmail, usPassword)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        Toast.makeText(RegisterUsr.this, "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                        DocumentReference df = firebaseFirestore.collection("Users").document(firebaseUser.getUid());
                        Map<String, Object> userInfo = new HashMap<>();
                        userInfo.put("FullName", fullName.getText().toString());
                        userInfo.put("id", firebaseAuth.getCurrentUser().getUid());
                        userInfo.put("imgUrl", imgLink);
                        userInfo.put("UserEmail", email.getText().toString());
                        userInfo.put("PhoneNumber", phone.getText().toString());
                        userInfo.put("Address", address.getText().toString());
                        userInfo.put("isAvailable", true);
                        userInfo.put("isUser", "1");
                        if (usDatecreated != null) {
                            userInfo.put("AccountDateCreated", usDatecreated);
                        }
                        df.set(userInfo);

                        DocumentReference df2 = firebaseFirestore.collection("Availability")
                                .document(firebaseUser.getUid());
                        Map<String, Object> userAvailablilityInfo = new HashMap<>();
                        userAvailablilityInfo.put("isAvailable", true);
                        df2.set(userAvailablilityInfo);
                        availRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(userAvailablilityInfo);

                        Map<String, Object> userProfile = new HashMap<>();
                        userProfile.put("id", firebaseAuth.getCurrentUser().getUid());
                        userProfile.put("imgUrl", imgLink);
                        userProfile.put("University", school.getText().toString());
                        userProfile.put("FullName", fullName.getText().toString());
                        userProfile.put("UserEmail", email.getText().toString());
                        userProfile.put("PhoneNumber", phone.getText().toString());
                        userProfile.put("DoB", etDob.getText().toString());
                        userProfile.put("Address", address.getText().toString());
                        userProfile.put("isAvailable", true);
                        if (usDatecreated != null) {
                            userProfile.put("AccountDateCreated", usDatecreated);
                        }
                        profileRef.child(firebaseAuth.getCurrentUser().getUid()).setValue(userProfile);

                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterUsr.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void getTextFromEditTexts() {
        usEmail = email.getText().toString();
        usPassword = password.getText().toString();
        usFullname = fullName.getText().toString();
        usPhone = phone.getText().toString();
        usDob = etDob.getText().toString();
        usAddress = address.getText().toString();
        usSchool = school.getText().toString();

    }

    private void init() {
        firebaseAuth = firebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();
        fDb = FirebaseDatabase.getInstance();
        profileRef = fDb.getReference("Profiles");
        authenRef = fDb.getReference("Authentications");
        availRef = fDb.getReference("Availability");
        date = new Date();
        Long date2 = date.getEpochSecond();
        usDatecreated = String.valueOf(date2);
    }

    private void mapping() {
        fullName = findViewById(R.id.etUsrFullname);
        etDob = findViewById(R.id.etDateofBirth);
        email = findViewById(R.id.etUsrEmail);
        password = findViewById(R.id.etPassword);
        phone = findViewById(R.id.etPersonalPhone);
        school = findViewById(R.id.etSchool);
        address = findViewById(R.id.etPersonalAddress);
        emCheck = findViewById(R.id.emailCheck);
        psCheck = findViewById(R.id.personalCheck);
        avatar = findViewById(R.id.avatarUser);
        registerButton = findViewById(R.id.btRegister);
        avatarUrl = findViewById(R.id.etAvatar);
    }

    private boolean valFullName() {
        String name = fullName.getText().toString();
        if (name.isEmpty()) {
            fullName.setError("Vui lòng nhập Họ và tên");
            return false;
        } else {
            fullName.setError(null);
            return true;
        }
    }

    private boolean valEmail() {
        String em = email.getText().toString().trim();
        String emailCond = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (em.isEmpty()) {
            email.setError("Vui lòng nhập Email");
            return false;
        } else if (!em.matches(emailCond)) {
            email.setError("Email không hợp lệ");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean valInputPass() {
        String pass = password.getText().toString().trim();
        if (pass.isEmpty()) {
            password.setError("Vui lòng nhập mật khẩu");
            return false;
        } else if (pass.length() < 6) {
            password.setError("Mật khẩu quá ngắn!");
            return false;
        } else if (!containLowerCase(pass) || !containUpperCase(pass) || !containSpecialChar(pass)
                || toStream(pass).anyMatch(c -> c.equals(" "))) {
            password.setError(
                    "Mật khẩu phải bao gồm ít nhất 1 ký tự viết hoa, viết thường, ký tự đặc biệt và không bao gồm \" \"");
            return false;
        }
        password.setError(null);
        return true;
    }

    private boolean valAddress() {
        String valAddress = address.getText().toString();
        if (valAddress.isEmpty()) {
            address.setError("Vui lòng nhập địa chỉ");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    private boolean valDateOfBirth() {
        String dateOfBirth = etDob.getText().toString();
        if (dateOfBirth.isEmpty()) {
            etDob.setError("Vui lòng nhập ngày tháng năm sinh");
            return false;
        } else {
            etDob.setError(null);
            return true;
        }
    }

    private boolean valPhoneNum() {
        String phoneNum = phone.getText().toString();
        if (phoneNum.isEmpty()) {
            phone.setError("Vui lòng nhập năm thành lập");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }

}
