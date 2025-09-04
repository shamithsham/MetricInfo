package ModelClass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.metricinfo.R;

import java.util.ArrayList;
import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {
    private List<Company> companyList;
    private List<Company> companyListFull;

    public CompanyAdapter(List<Company> companyList) {
        this.companyList = companyList;
        this.companyListFull = new ArrayList<>(companyList);
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout, parent, false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        Company company = companyList.get(position);
        holder.name.setText(company.getClientName());
    }

    @Override
    public int getItemCount() {
        return companyList.size();
    }

    public void filter(String text) {
        companyList.clear();
        if (text.isEmpty()) {
            companyList.addAll(companyListFull);
        } else {
            text = text.toLowerCase();
            for (Company company : companyListFull) {
                if (company.getClientName().toLowerCase().contains(text)) {
                    companyList.add(company);
                }
            }
        }
        notifyDataSetChanged();
    }



    static class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView name,phone,mail;
        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvContactPerson);
            phone = itemView.findViewById(R.id.tvPhone);
            mail = itemView.findViewById(R.id.tvEmail);
        }
    }
}
