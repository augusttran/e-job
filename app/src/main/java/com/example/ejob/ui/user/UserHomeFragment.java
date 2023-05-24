
package com.example.ejob.ui.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ejob.R;
import com.example.ejob.ui.employer.job.JobPosting;
import com.example.ejob.ui.user.userjob.AllJobAdapter;
import com.example.ejob.ui.user.userjob.JobPostingforUser;
import com.example.ejob.ui.user.userjob.UserAllJobViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView jobRecyclerView;
    private UserAllJobViewModel userAllJobView;
    private AllJobAdapter allJobAdapter;

    private ViewGroup viewgroupContainer;
    private LayoutInflater layoutInflater;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading;
    private boolean isLastPage;
    private int totalPage = 10;
    private int currentPage = 1;

    View v;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHomeFragment() {
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
    public static UserHomeFragment newInstance(String param1, String param2) {
        UserHomeFragment fragment = new UserHomeFragment();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.v = view;
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeUserJob);

        jobRecyclerView = (RecyclerView) v.findViewById(R.id.rcvJobUser);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        jobRecyclerView.setLayoutManager(linearLayoutManager);
        jobRecyclerView.setHasFixedSize(true);
        userAllJobView = new ViewModelProvider(this).get(UserAllJobViewModel.class);

        // allJobAdapter = new AllJobAdapter();
        userAllJobView.getmListJobLivedata().observe(getViewLifecycleOwner(), new Observer<List<JobPostingforUser>>() {
            @Override
            public void onChanged(List<JobPostingforUser> jobPostings) {
                // allJobAdapter.setData(jobPostings);
                allJobAdapter = new AllJobAdapter(jobPostings, new AllJobAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(JobPostingforUser jobPost) {
                        FragmentManager fragmentManager = getChildFragmentManager();
                        layoutInflater.inflate(R.layout.fragment_application_job, viewgroupContainer, false);
                        Toast.makeText(UserHomeFragment.this.getContext(), jobPost.getSalary(), Toast.LENGTH_LONG)
                                .show();
                        Log.d("TAG_UHFragment", jobPost.getJobTitle());
                    }

                });
                jobRecyclerView.setAdapter(allJobAdapter);
                jobRecyclerView.smoothScrollToPosition(0);

            }
        });

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(v.getContext(),
                DividerItemDecoration.VERTICAL);
        jobRecyclerView.addItemDecoration(itemDecoration);

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allJobAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        viewgroupContainer = container;
        layoutInflater = inflater;
        return inflater.inflate(R.layout.fragment_job_detail, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allJobAdapter.notifyDataSetChanged();
            }
        });
    }

}