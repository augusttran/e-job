package com.example.ejob.ui.employer;

import android.os.Bundle;
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
import com.example.ejob.ui.employer.applications.ApplicationAdapter;
import com.example.ejob.ui.employer.applications.ApplicationViewModel_2;
import com.example.ejob.ui.user.application.JobApplication;

import java.util.List;

public class ShortlistedApps extends androidx.fragment.app.Fragment {
    private TextView jobTitle, jobId, jobType;
    private RecyclerView applicants;

    ApplicationAdapter applicationAdapter;
    ApplicationViewModel_4 applicationViewModel_4;
    private ViewGroup viewgroupContainer;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeRefreshLayout;

    View v;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShortlistedApps() {
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
    public static ShortlistedApps newInstance(String param1, String param2) {
        ShortlistedApps fragment = new ShortlistedApps();
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
        return inflater.inflate(R.layout.fragment_shortlisted_applications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeJoblistEmployer);

        jobTitle = v.findViewById(R.id.tvJobTitleA);
        applicants = v.findViewById(R.id.rcvJobsEmp2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        applicants.setLayoutManager(linearLayoutManager);

        applicationViewModel_4 = new ViewModelProvider(this).get(ApplicationViewModel_4.class);
        applicationViewModel_4.getmListApplicationsLivedata().observe(getViewLifecycleOwner(),
                new Observer<List<JobApplication>>() {
                    @Override
                    public void onChanged(List<JobApplication> jobs) {
                        applicationAdapter = new ApplicationAdapter(jobs, new ApplicationAdapter.ItemClickListener() {

                            @Override
                            public void onItemClick(JobApplication application) {

                            }
                        });

                        applicants.setAdapter(applicationAdapter);

                    }
                });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                applicationAdapter.notifyDataSetChanged();
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
                applicationAdapter.notifyDataSetChanged();
            }
        });
    }

}
