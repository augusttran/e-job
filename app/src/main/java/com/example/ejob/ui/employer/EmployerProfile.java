package com.example.ejob.ui.employer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ejob.R;
import com.example.ejob.ui.employer.applications.MyJobsAdapter;
import com.example.ejob.ui.employer.applications.MyJobsViewModel;
import com.example.ejob.ui.user.userjob.JobPostingforUser;

import java.util.List;

public class EmployerProfile extends androidx.fragment.app.Fragment {

    private TextView jobTitle, jobId, jobType;
    private RecyclerView jobRcv;

    MyJobsAdapter myJobsAdapter;
    MyJobsViewModel myJobsViewModel;
    private ViewGroup viewgroupContainer;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvEmp;

    View v;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmployerProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobApplicationEmp.
     */
    // TODO: Rename and change types and number of parameters
    public static EmployerProfile newInstance(String param1, String param2) {
        EmployerProfile fragment = new EmployerProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewgroupContainer = container;
        layoutInflater = inflater;
        return inflater.inflate(R.layout.fragment_emp_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe222);

        jobTitle = v.findViewById(R.id.tvJobTitleA);
        jobRcv = v.findViewById(R.id.rcv2222);

        tvEmp = v.findViewById(R.id.tvEmpProfile);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        jobRcv.setLayoutManager(linearLayoutManager);

        myJobsViewModel = new ViewModelProvider(this).get(MyJobsViewModel.class);
        myJobsViewModel.getmListJobLivedata().observe(getViewLifecycleOwner(), new Observer<List<JobPostingforUser>>() {
            @Override
            public void onChanged(List<JobPostingforUser> jobs) {
                myJobsAdapter = new MyJobsAdapter(jobs, new MyJobsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(JobPostingforUser jobPost) {

                    }
                });
            }
        });

        jobRcv.setAdapter(myJobsAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myJobsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myJobsAdapter.notifyDataSetChanged();
            }
        });
    }

}
