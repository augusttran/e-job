package com.example.ejob.ui.employer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ejob.R;
import com.example.ejob.ui.employer.job.JobAdapter;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.employer.job.JobViewModel;
import com.example.ejob.ui.user.UserHomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EmployerHome extends androidx.fragment.app.Fragment implements IEmployerHome {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FloatingActionButton logout, newJob;
    RecyclerView jobRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    View v;
    LayoutInflater layoutInflater;
    ViewGroup container;
    private JobViewModel jobViewModel;
    private JobAdapter jobAdapter;
    private String mParam1;
    private String mParam2;

    public EmployerHome() {
        // Required empty public constructor
    }

    public static EmployerHome newInstance(String param1, String param2) {
        EmployerHome fragment = new EmployerHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        this.layoutInflater = inflater;
        this.container = container;

        return inflater.inflate(R.layout.activity_home_employer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;
        // logout = v.findViewById(R.id.btnLogout_employer);
        newJob = v.findViewById(R.id.btnAddjob);
        jobRecyclerView = v.findViewById(R.id.rcJoblist);
        swipeRefreshLayout = v.findViewById(R.id.swipeJoblist);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        jobRecyclerView.setLayoutManager(linearLayoutManager);

        jobViewModel = new ViewModelProvider(this).get(JobViewModel.class);
        jobViewModel.getmListJobLivedata().observe(this.getViewLifecycleOwner(), new Observer<List<JobPosting>>() {
            @Override
            public void onChanged(List<JobPosting> jobPostings) {
                jobAdapter = new JobAdapter(jobPostings, new JobAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(JobPosting jobPost) {
                        Toast.makeText(v.getContext(), jobPost.getJobTitle(), Toast.LENGTH_LONG).show();

                    }
                });

                jobRecyclerView.setAdapter(jobAdapter);

            }

        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        newJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(v.getContext(), AddJob.class));
                getActivity().finish();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jobAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void viewApplication() {
        increaseViewNumber();
    }

    private void increaseViewNumber() {

    }

    @Override
    public void viewApplicant() {

    }

    @Override
    public void viewJob() {

    }
}
