package com.josef.mobile.free;

import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.josef.mobile.data.Favourite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class ViewModelContent extends ViewModel {

    public static final String JSON_METADATA = "metadata";
    public static final String JSON_NAME = "name";
    public static final String JSON_PNG = "png";
    public static final String JSON_URL = "url";


    protected void addData(List<Favourite> favourites, final String id) throws JSONException {
        FirebaseDatabase database = FirebaseDatabase.getInstance();//
        final DatabaseReference myRef = database.getReference("users");
        Data data = getData(favourites);
        myRef.child(id).setValue(data);
    }


    private Data getData(List<Favourite> favourites) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray googlevideos = new JSONArray();
        JSONObject data = new JSONObject();
        JSONArray sources = new JSONArray();
        int len = favourites.size();
        for (int i = 0; i <= len - 1; i++) {
            JSONObject sum = new JSONObject();
            sum.put("description", "LoremIpsum...");
            JSONArray path = new JSONArray();
            path.put(favourites.get(i).getTitle());
            sum.put("sources", path);
            sum.put("card", favourites.get(i).getDescription());
            sum.put("background", favourites.get(i).getDescription());
            sum.put("title", "material");
            sum.put("studio", "Google+");
            sources.put(sum);
        }
        data.put("category", "Google+");
        data.put("videos", sources);
        googlevideos.put(data);
        jsonObject.put("googlevideos", googlevideos);
        return new Data(jsonObject.toString(), "", "");
    }
}

