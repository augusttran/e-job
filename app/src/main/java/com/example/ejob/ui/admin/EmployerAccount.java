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
import com.example.ejob.data.model.EmployerModel;
import com.example.ejob.ui.admin.employer_accounts.Employer;
import com.example.ejob.ui.admin.employer_accounts.EmployerAdapter;
import com.example.ejob.ui.admin.employer_accounts.EmployerViewModel;
import com.example.ejob.ui.user.UserHomeFragment;
import com.example.ejob.ui.user.userjob.AllJobAdapter;
import com.example.ejob.ui.user.userjob.UserAllJobViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class EmployerAccount extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth fAuth;

    private RecyclerView userRecyclerView;
    private EmployerViewModel allEmployerViewModel;
    private EmployerAdapter employerAdapter;

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
        return inflater.inflate(R.layout.fragment_view_employer_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeEmployerList);

        userRecyclerView = (RecyclerView) v.findViewById(R.id.rcvUserAccount);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        userRecyclerView.setLayoutManager(linearLayoutManager);
        allEmployerViewModel = new ViewModelProvider(this).get(EmployerViewModel.class);

        allEmployerViewModel.getmListJobLivedata().observe(getViewLifecycleOwner(),
                new Observer<List<EmployerModel>>() {
                    @Override
                    public void onChanged(List<EmployerModel> employers) {
                        employerAdapter = new EmployerAdapter(employers, new EmployerAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(Employer employer) {
                                FragmentManager fragmentManager = getChildFragmentManager();
                                Toast.makeText(EmployerAccount.this.getContext(), employer.getEmployerName(),
                                        Toast.LENGTH_LONG).show();
                                Log.d("TAG_UHFragment", employer.getEmployerName());
                            }
                        });
                        userRecyclerView.setAdapter(employerAdapter);

                    }
                });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                employerAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
