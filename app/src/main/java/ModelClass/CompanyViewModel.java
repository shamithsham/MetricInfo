package ModelClass;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.ArrayList;

public class CompanyViewModel extends AndroidViewModel {
    private CompanyRepository repository;
    private LiveData<ArrayList<Company>> companies;

    public CompanyViewModel(@NonNull Application application) {
        super(application);
        repository = CompanyRepository.getInstance();
        companies = repository.getCompaniesLiveData();
    }

    public LiveData<ArrayList<Company>> getCompanies() {
        return companies;
    }

    public void fetchCompanies() {
        repository.fetchCompanies(getApplication());
    }
    public void filterCompanies(String query) {
        repository.filterCompanies(query);
    }
}
