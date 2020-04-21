package com.josef.mobile.net;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.josef.mobile.MainActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.josef.mobile.Config.VIEWPAGER_AMOUNT;

/**http://joseph3d.com/wp-admin/
 Benutzer: joseph
 Passwort: %(d2cIVsYKJdVNTZDhd@BR8d**/
public class CallBackWorkerSplashActivity extends ListenableWorker {

    public CallBackWorkerSplashActivity(Context context, WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        return CallbackToFutureAdapter.getFuture(new CallbackToFutureAdapter.Resolver<Result>() {

            final Data data = new Data.Builder().build();
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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(VIEWPAGER_AMOUNT,3-1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
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
}
