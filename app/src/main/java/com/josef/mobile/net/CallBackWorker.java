package com.josef.mobile.net;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


import static com.josef.mobile.Config.KEY_TASK_OUTPUT;
import static com.josef.mobile.Config.WORKREQUEST_AMOUNT;

/**http://joseph3d.com/wp-admin/
 Benutzer: joseph
 Passwort: %(d2cIVsYKJdVNTZDhd@BR8d**/
public class CallBackWorker extends ListenableWorker {
    private Gson mGson = new Gson();

    public CallBackWorker(Context context, WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        Data data = getInputData();
        final int index = data.getInt(WORKREQUEST_AMOUNT,0);


        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<Result>() {
            @Nullable
            @Override
            public Object attachCompleter(@NonNull final CallbackToFutureAdapter.Completer<Result> completer) throws Exception {

                Callback callback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                            completer.set(Result.failure());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                          String output = mGson.toJson(input(index));
                          final Data data = buildData(output);
                            completer.set(Result.success(data));
                    }
                };

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://joseph3d.com/wp-admin/")
                        .build();

                client.newCall(request).enqueue(callback);

                return callback;
            }
        });
    }
    private ArrayList<String> input(int index){

        ArrayList<String> nestedList = new ArrayList();
        ArrayList<ArrayList<String>> dataSet = new ArrayList<>();
        for (int i = 0; i <= 50-1; i++) {
            nestedList.add("http://joseph3d.com/wp-content/uploads/2019/06/00010621.png");
        }
        for (int i = 0; i <= 10-1 ; i++) {
            dataSet.add(nestedList);
        }
        return dataSet.get(index);
    }


    private Data buildData(String strings) {
        return new Data.Builder()
                .putString(KEY_TASK_OUTPUT,strings)
                .build();
    }
}