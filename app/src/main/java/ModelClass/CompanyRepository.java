package ModelClass;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class CompanyRepository {
    private static CompanyRepository instance;
    private MutableLiveData<ArrayList<Company>> companiesLiveData = new MutableLiveData<>();
    private ArrayList<Company> allCompanies = new ArrayList<>();

    public static CompanyRepository getInstance() {
        if (instance == null) {
            instance = new CompanyRepository();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Company>> getCompaniesLiveData() {
        return companiesLiveData;
    }

    public void fetchCompanies(Context context) {
        String url = "https://api.metricinfo.com/api/Client/Get?UserId=USR5021&PageSize=1000&CurrentPage=1&EnabledStatus=1";

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Company> list = new ArrayList<>();
                        try {
                            JSONArray data = response.getJSONArray("result");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                String clientId = obj.getString("clientID");
                                String clientName = obj.getString("clientName");
                                list.add(new Company(clientId, clientName));
                            }
                            allCompanies = list;
                            companiesLiveData.postValue(list);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            companiesLiveData.postValue(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API_ERROR", error.toString());
                        companiesLiveData.postValue(null);
                    }
                }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Travelize_Authentication", "P/xgewt8+bhG6xYVveE+lLIjQSIKaUnoj9f8NHYHqQo8uz/gpTuRBMDBZhpDm6QuLgamO/z/X6HgJT5NFtUSV9hTc7a/EtuKRXwzbtt8jXe2epiA5dOkebouO8Hl5QM4+lhHngEiz1RHQ47tc1jTVIDBU7WHCtvJPFex/ez13NB+TTXptqHAse8K9vZ4FsWNXcV4gCQL+FJg2c2pCUQNmLe9pCqYbt5iGggMYOkquaMovrlI3mKRtwX8X04nJeBGnjKPaje51a7UUCBuJigfiUbc1kIY4gyafNWxhCVl80CZ4/3lHjaNYIhxgf0aUWOQ");
                return headers;
            }
        };

        queue.add(request);


    }
    public void filterCompanies(String query) {
        if (query == null || query.isEmpty()) {
            companiesLiveData.postValue(allCompanies);
            return;
        }


        ArrayList<Company> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase();

        for (Company company : allCompanies) {
            if (company.getClientName().toLowerCase().contains(lowerCaseQuery) ||
                    company.getClientId().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(company);
            }
        }
        companiesLiveData.postValue(filteredList);
    }
}
