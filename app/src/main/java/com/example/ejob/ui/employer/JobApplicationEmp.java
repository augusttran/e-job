package com.example.ejob.ui.employer;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejob.R;
import com.example.ejob.ui.employer.applications.MyJobsAdapter;
import com.example.ejob.ui.employer.applications.MyJobsViewModel;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.ui.user.userjob.UserAllJobViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobApplicationEmp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobApplicationEmp extends androidx.fragment.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MyJobsAdapter myJobsAdapter;
    MyJobsViewModel myJobsViewModel;
    View v;
    private TextView jobTitle, jobId, jobType;
    private RecyclerView jobRcv;
    private ViewGroup viewgroupContainer;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeRefreshLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JobApplicationEmp() {
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
    public static JobApplicationEmp newInstance(String param1, String param2) {
        JobApplicationEmp fragment = new JobApplicationEmp();
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
        return inflater.inflate(R.layout.fragment_job_application_emp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeJoblistEmployer);

        jobTitle = v.findViewById(R.id.tvJobTitleA);
        jobRcv = v.findViewById(R.id.rcvJobsEmp);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        jobRcv.setLayoutManager(linearLayoutManager);

        myJobsViewModel = new ViewModelProvider(this).get(MyJobsViewModel.class);
        myJobsViewModel.getmListJobLivedata().observe(getViewLifecycleOwner(), new Observer<List<JobPostingforUser>>() {
            @Override
            public void onChanged(List<JobPostingforUser> jobs) {
                myJobsAdapter = new MyJobsAdapter(jobs, new MyJobsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(JobPostingforUser jobPost) {
                        Toast.makeText(v.getContext(), jobPost.getJobTitle(), Toast.LENGTH_LONG).show();

                    }
                });

                jobRcv.setAdapter(myJobsAdapter);

            }
        });

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