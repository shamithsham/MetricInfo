package com.example.metricinfo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import ModelClass.Company;
import ModelClass.CompanyAdapter;
import ModelClass.CompanyViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class TeamFragment extends Fragment {

    private CompanyViewModel viewModel;
    private CompanyAdapter adapter;
    private ArrayList<Company> companyList = new ArrayList<>();
    private SearchView searchView;
    private ExtendedFloatingActionButton fabAddCompany;

    private LinearLayout llSearchContainer, llNearbyContainer;
    private ImageView ivSearchIcon, ivFilterTop, ivNearbyIcon;
    private TextView tvNearby;

    private boolean isCollapsed = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rvCompanies);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new CompanyAdapter(companyList);
        recyclerView.setAdapter(adapter);

        fabAddCompany = view.findViewById(R.id.fabAddCompany);

        llSearchContainer = view.findViewById(R.id.llSearchContainer);
        llNearbyContainer = view.findViewById(R.id.llNearbyContainer);
        ivSearchIcon = view.findViewById(R.id.ivSearchIcon);
        ivFilterTop = view.findViewById(R.id.ivFilterTop);
        tvNearby = view.findViewById(R.id.tvNearby);
        ivNearbyIcon = view.findViewById(R.id.ivNearbyIcon);

        searchView = view.findViewById(R.id.searchView);
        int id = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        TextView searchText = searchView.findViewById(id);
        searchText.setTextColor(Color.BLACK);
        searchText.setHintTextColor(Color.GRAY);

        setupScrollListener(recyclerView);
        setupSearchIcon();
        setupSearchView();

        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        setupViewModelObserver();
        viewModel.fetchCompanies();

        return view;
    }

    private void setupScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 15) {

                    fabAddCompany.shrink();
                    if (isCollapsed) expandAll();
                } else if (dy < -15) {

                    fabAddCompany.extend();
                    if (!isCollapsed) collapseAll();
                }
            }
        });
    }

    private void setupSearchIcon() {
        ivSearchIcon.setOnClickListener(v -> {
            expandAll();
            searchView.requestFocus();
            searchView.onActionViewExpanded();
        });
    }

    private void collapseAll() {
        if (isCollapsed) return;
        isCollapsed = true;


        ObjectAnimator slideUp = ObjectAnimator.ofFloat(llSearchContainer, "translationY", 0f, -llSearchContainer.getHeight());
        ObjectAnimator fadeOutSearch = ObjectAnimator.ofFloat(llSearchContainer, "alpha", 1f, 0f);


        ObjectAnimator fadeOutNearby = ObjectAnimator.ofFloat(tvNearby, "alpha", 1f, 0f);
        ObjectAnimator fadeOutLocation = ObjectAnimator.ofFloat(ivNearbyIcon, "alpha", 1f, 0f);

        AnimatorSet collapseSet = new AnimatorSet();
        collapseSet.playTogether(slideUp, fadeOutSearch, fadeOutNearby, fadeOutLocation);
        collapseSet.setDuration(300);
        collapseSet.setInterpolator(new AccelerateDecelerateInterpolator());

        collapseSet.start();


        ivSearchIcon.postDelayed(() -> {
            ivSearchIcon.setVisibility(View.VISIBLE);
            ivFilterTop.setVisibility(View.VISIBLE);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivSearchIcon, "scaleX", 0f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivSearchIcon, "scaleY", 0f, 1f);
            ObjectAnimator fadeInSearchIcon = ObjectAnimator.ofFloat(ivSearchIcon, "alpha", 0f, 1f);
            ObjectAnimator fadeInFilter = ObjectAnimator.ofFloat(ivFilterTop, "alpha", 0f, 1f);

            AnimatorSet showIcons = new AnimatorSet();
            showIcons.playTogether(scaleX, scaleY, fadeInSearchIcon, fadeInFilter);
            showIcons.setDuration(200);
            showIcons.start();
        }, 150);
    }

    private void expandAll() {
        if (!isCollapsed) return;
        isCollapsed = false;

        ObjectAnimator fadeOutSearchIcon = ObjectAnimator.ofFloat(ivSearchIcon, "alpha", 1f, 0f);
        ObjectAnimator fadeOutFilter = ObjectAnimator.ofFloat(ivFilterTop, "alpha", 1f, 0f);

        AnimatorSet hideIcons = new AnimatorSet();
        hideIcons.playTogether(fadeOutSearchIcon, fadeOutFilter);
        hideIcons.setDuration(200);
        hideIcons.setInterpolator(new AccelerateDecelerateInterpolator());

        hideIcons.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ivSearchIcon.setVisibility(View.GONE);
                ivFilterTop.setVisibility(View.GONE);


                llSearchContainer.postDelayed(() -> {
                    ObjectAnimator slideDown = ObjectAnimator.ofFloat(llSearchContainer, "translationY", -llSearchContainer.getHeight(), 0f);
                    ObjectAnimator fadeInSearch = ObjectAnimator.ofFloat(llSearchContainer, "alpha", 0f, 1f);

                    ObjectAnimator fadeInNearby = ObjectAnimator.ofFloat(tvNearby, "alpha", 0f, 1f);
                    ObjectAnimator fadeInLocation = ObjectAnimator.ofFloat(ivNearbyIcon, "alpha", 0f, 1f);

                    AnimatorSet expandSet = new AnimatorSet();
                    expandSet.playTogether(slideDown, fadeInSearch, fadeInNearby, fadeInLocation);
                    expandSet.setDuration(300);
                    expandSet.start();
                }, 100);
            }
        });
        hideIcons.start();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.filterCompanies(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.filterCompanies(newText);
                return true;
            }
        });
    }


    private void setupViewModelObserver() {
        viewModel.getCompanies().observe(getViewLifecycleOwner(), companies -> {
            if (companies != null) {
                companyList.clear();
                companyList.addAll(companies);
                adapter.notifyDataSetChanged();

                String query = searchView.getQuery().toString();
                if (!query.isEmpty()) {
                    viewModel.filterCompanies(query);
                }
            }
        });
    }
}
