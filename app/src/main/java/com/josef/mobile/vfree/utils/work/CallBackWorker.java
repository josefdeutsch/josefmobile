package com.josef.mobile.vfree.utils.work;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.josef.mobile.vfree.ui.err.ErrorActivity;
import com.josef.mobile.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallBackWorker extends ListenableWorker implements Worker {

    private final Context context;

    public CallBackWorker(Context context, WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {

        Data data = getInputData();
        final String name = data.getString(WORKREQUEST_INDICATOR);

        return CallbackToFutureAdapter.getFuture(completer -> {

            Callback callback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    final Data data = buildData(WORKREQUEST_KEYTASK_ERROR, e.toString());
                    completer.set(Result.failure(data));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() >= MAX_VALID_CASES) {
                        final Data data = buildData(WORKREQUEST_KEYTASK_ERROR,
                                context.getResources().getString(R.string.callbackworker_url_error));
                        Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
                        intent.putExtra(WORKREQUEST_INDICATOR, name);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        completer.set(Result.failure(data));
                    }

                    final Data data = buildData(WORKREQUEST_KEYTASK_SUCCESS,
                            name);
                    completer.set(Result.success(data));
                }
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(context.getResources().getString(R.string.callbackworker_url_direct))
                    .build();

            client.newCall(request).enqueue(callback);
            return callback;

        });
    }

    private Data buildData(String key, String value) {
        return new Data.Builder()
                .putString(key, value)
                .build();
    }
}
