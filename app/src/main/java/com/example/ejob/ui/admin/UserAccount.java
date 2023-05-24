package com.example.ejob.ui.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ejob.R;
import com.example.ejob.data.model.ApplicantModel;
import com.example.ejob.data.model.EmployerModel;
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.ui.admin.employer_accounts.EmployerAdapter;
import com.example.ejob.ui.admin.employer_accounts.EmployerViewModel;
import com.example.ejob.ui.admin.user_accounts.UserAdapter;
import com.example.ejob.ui.admin.user_accounts.UserModel;
import com.example.ejob.ui.admin.user_accounts.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserAccount extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth fAuth;

    private RecyclerView userRecyclerView;
    private UserViewModel allUserViewModel;
    private UserAdapter userAdapter;

    private ViewGroup viewgroupContainer;
    private LayoutInflater layoutInflater;
    View v;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        viewgroupContainer = container;
        layoutInflater = inflater;
        return inflater.inflate(R.layout.fragment_view_user_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeUserList);

        userRecyclerView = (RecyclerView) v.findViewById(R.id.rcvUserAccount1);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        userRecyclerView.setLayoutManager(linearLayoutManager);
        allUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        allUserViewModel.getmListJobLivedata().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
            @Override
            public void onChanged(List<UserModel> employers) {
                userAdapter = new UserAdapter(employers, new UserAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(Employer employer) {
                        FragmentManager fragmentManager = getChildFragmentManager();
                        Toast.makeText(UserAccount.this.getContext(), employer.getEmployerName(), Toast.LENGTH_LONG)
                                .show();
                        Log.d("TAG_UHFragment", employer.getEmployerName());
                    }
                });
                userRecyclerView.setAdapter(userAdapter);

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
