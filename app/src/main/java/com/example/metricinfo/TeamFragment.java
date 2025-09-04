package com.example.metricinfo;

import android.graphics.Color;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import ModelClass.Company;
import ModelClass.CompanyAdapter;
import ModelClass.CompanyViewModel;

public class TeamFragment extends Fragment {

    private CompanyViewModel viewModel;
    private CompanyAdapter adapter;
    private ArrayList<Company> companyList = new ArrayList<>();
    private SearchView searchView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_team, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.rvCompanies);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new CompanyAdapter(companyList);
        recyclerView.setAdapter(adapter);

        searchView = view.findViewById(R.id.searchView);
        // Get the SearchAutoComplete inside SearchView
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);

        TextView searchText = searchView.findViewById(id);

// Set text color and hint color
        searchText.setTextColor(Color.BLACK);       // Text color
        searchText.setHintTextColor(Color.GRAY);
        setupSearchView();

        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        setupViewModelObserver();

        viewModel.fetchCompanies();


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

        viewModel = new ViewModelProvider(this).get(CompanyViewModel.class);
        viewModel.getCompanies().observe(requireActivity(), companies -> {
            if (companies != null) {
                companyList.clear();
                companyList.addAll(companies);
                adapter.notifyDataSetChanged();

                String query = searchView.getQuery().toString();
                if (!query.isEmpty()) {
                    adapter.filter(query);
                }
            }
        });

        viewModel.fetchCompanies();



        return view;
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
            }
        });
    }

}