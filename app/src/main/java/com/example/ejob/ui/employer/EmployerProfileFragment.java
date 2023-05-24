package com.example.ejob.ui.employer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ejob.R;
import com.example.ejob.data.ProfileAdapter;
import com.example.ejob.data.ProfileViewModel;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.data.model.EmployerModel;
import com.example.ejob.utils.Date;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmployerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmployerProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int CHOOSE_PDF_FROM_DEVICE = 1001;
    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    EmployerModel model;
    ImageView userAvatar, upload, change;
    boolean cvExist = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View v;
    private Button pdfBrowse, uploadButton, updateProfile;
    private TextView filepath, cvTitle, cv;
    private EditText email, dob, phone, address, fullname, quymo, website, industry, accountcreated;
    Pair<ApplicantModel, String> pair;
    ProfileViewModel profileViewModel;
    ProfileAdapter profileAdapter;
    Date date;
    Long dateCreated;

    public EmployerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHomeFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static EmployerProfileFragment newInstance(String param1, String param2) {
        EmployerProfileFragment fragment = new EmployerProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        date = new Date();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        // userAvatar = (ImageView) v.findViewById(R.id.userAvatar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_employer_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.v = view;

        mapping();
        init();
        fetchData();

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(valFullName() && valAddress() && valEmail() && valWebsite() && valDob())) {
                    return;
                } else {
                    updateEvent();
                }

            }
        });

    }

    private void updateEvent() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Email", email.getText().toString());
        hashMap.put("Address", address.getText().toString());
        hashMap.put("Tencongty", fullname.getText().toString());
        hashMap.put("Quymo", quymo.getText().toString());
        hashMap.put("PhoneNumber", phone.getText().toString());
        hashMap.put("Industry", industry.getText().toString());
        hashMap.put("Account", fullname.getText().toString());
        hashMap.put("Website", website.getText().toString());
        DatabaseReference profileRef = firebaseDatabase.getReference("Profiles")
                .child(firebaseAuth.getCurrentUser().getUid());
        profileRef.updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(v.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private TextWatcher updateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (valAddress() && valFullName() && valWebsite() && valDob() && valEmail()
                    && valIndustry() && valIndustry() && valSize() && valYof()) {
                gatherData();
                updateProfile.setEnabled(true);

            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private EmployerModel gatherData() {
        EmployerModel profile = new EmployerModel();
        profile.setEmployerEmail(email.getText().toString());
        profile.setEmployerAddress(address.getText().toString());
        profile.setEmployerFullname(fullname.getText().toString());
        profile.setEmployerIndustry(industry.getText().toString());
        profile.setEmployerWebsite(website.getText().toString());
        profile.setEmployerPhone(phone.getText().toString());
        profile.setYearofFoundation(dob.getText().toString());
        profile.setDateCreationEmployer(accountcreated.getText().toString());
        // HashMap<String, Object> hashMap = new HashMap<>();
        // hashMap.put("Email", email.getText().toString());
        // hashMap.put("Address", address.getText().toString());
        // hashMap.put("Tencongty", fullname.getText().toString());
        // hashMap.put("Quymo", quymo.getText().toString());
        // hashMap.put("PhoneNumber", phone.getText().toString());
        // hashMap.put("Industry", industry.getText().toString());
        // hashMap.put("Account", fullname.getText().toString());
        // hashMap.put("Quymo", quymo.getText().toString());

        return profile;
    }

    private boolean valFullName() {
        String name = fullname.getText().toString();
        if (name.isEmpty()) {
            fullname.setError("Vui lòng nhập Tên công ty");
            return false;
        } else {
            fullname.setError(null);
            return true;
        }
    }

    private boolean valEmail() {
        String name = email.getText().toString();
        if (name.isEmpty()) {
            email.setError("Vui lòng nhập Email");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean valYof() {
        String name = dob.getText().toString();
        if (name.isEmpty()) {
            dob.setError("Vui lòng nhập năm thành lập");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }

    private boolean valSize() {
        String name = quymo.getText().toString();
        if (name.isEmpty()) {
            quymo.setError("Vui lòng nhập quy mô ");
            return false;
        } else {
            quymo.setError(null);
            return true;
        }
    }

    private boolean valIndustry() {
        String name = industry.getText().toString();
        if (name.isEmpty()) {
            industry.setError("Vui lòng nhập ngành nghề");
            return false;
        } else {
            industry.setError(null);
            return true;
        }
    }

    private boolean valPhone() {
        String name = phone.getText().toString();
        if (name.isEmpty()) {
            phone.setError("Vui lòng nhập số điện thoại");
            return false;
        } else {
            phone.setError(null);
            return true;
        }
    }

    private boolean valAddress() {
        String name = address.getText().toString();
        if (name.isEmpty()) {
            address.setError("Vui lòng nhập Địa chỉ");
            return false;
        } else {
            address.setError(null);
            return true;
        }
    }

    private boolean valDob() {
        String name = dob.getText().toString();
        if (name.isEmpty()) {
            dob.setError("Vui lòng nhập Năm thành lập");
            return false;
        } else {
            dob.setError(null);
            return true;
        }
    }

    private boolean valWebsite() {
        String name = website.getText().toString();
        if (name.isEmpty()) {
            website.setError("Vui lòng nhập Họ và tên");
            return false;
        } else {
            website.setError(null);
            return true;
        }
    }

    private void init() {
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("pdf");
        firestore = FirebaseFirestore.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        model = new EmployerModel();
        email.setEnabled(false);
        accountcreated.setEnabled(false);
        // swipeRefreshLayout = v.findViewById(R.id.swipeProfile);

    }

    private void mapping() {

        fullname = v.findViewById(R.id.etFullnameUP1);
        email = v.findViewById(R.id.etEmailUP1);
        phone = v.findViewById(R.id.etCompanyPhone);
        address = v.findViewById(R.id.etCompanyAddress);
        dob = v.findViewById(R.id.etYOF);
        website = v.findViewById(R.id.etWebcongty);
        industry = v.findViewById(R.id.etIndustry);
        accountcreated = v.findViewById(R.id.etNgaydangkyCty);
        quymo = v.findViewById(R.id.etQuymo1);
        updateProfile = v.findViewById(R.id.btnUpdate1);

    }

    private EmployerModel fetchData() {

        DatabaseReference profileRef = firebaseDatabase.getReference("Profiles");

        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseAuth.getCurrentUser().getUid()).exists()) {
                    for (DataSnapshot children : snapshot.child(firebaseAuth.getCurrentUser().getUid()).getChildren()) {
                        fullname.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("Tencongty")
                                .getValue().toString());
                        email.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("Email").getValue()
                                .toString());
                        phone.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("PhoneNumber")
                                .getValue().toString());
                        dob.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("Namthanhlap")
                                .getValue().toString());
                        address.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("Address")
                                .getValue().toString());
                        website.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("Website")
                                .getValue().toString());
                        industry.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("Industry")
                                .getValue().toString());
                        quymo.setText(snapshot.child(firebaseAuth.getCurrentUser().getUid()).child("Quymo").getValue()
                                .toString());
                        dateCreated = Long.parseLong(snapshot.child(firebaseAuth.getCurrentUser().getUid())
                                .child("AccountDateCreated").getValue().toString());
                        accountcreated.setText(date.getInstance(dateCreated).toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return model;
    }

}