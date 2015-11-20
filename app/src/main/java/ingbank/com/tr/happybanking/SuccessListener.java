package ingbank.com.tr.happybanking;

import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import volley.Response;


public abstract class SuccessListener<T> implements Response.Listener {
    Type type;

    public void setResponseType(Type type) {
        this.type = type;
    }

    @Override
    public void onResponse(Object response) {
        Log.d("Response: ", response.toString());
        Gson gson = new GsonBuilder().create();
        if (type == null) {
            type = Object.class;
        }
        T successType = gson.fromJson(response.toString(), type);
        onSuccess(successType);
    }

    public abstract void onSuccess(T type);

}