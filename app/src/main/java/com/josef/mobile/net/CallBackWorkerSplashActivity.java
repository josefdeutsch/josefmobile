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
import com.josef.mobile.Echo;
import com.josef.mobile.ErrorActivity;
import com.josef.mobile.Message;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import static com.josef.mobile.Config.KEY_TASK_ERROR;
import static com.josef.mobile.Config.KEY_TASK_OUTPUT;


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
                        final Data data = buildData(KEY_TASK_ERROR,e.toString());
                        completer.set(Result.failure(data));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() >= 301) {
                            final Data data = buildData(KEY_TASK_ERROR,"invalid server response..");
                            Intent intent = new Intent(getApplicationContext(), ErrorActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);
                            completer.set(Result.failure(data)); // nullpointer exception
                        }

                        Echo echo = new Echo();
                        Message message = echo.echo(new Message(),0);
                        String serialized = message.getMessage();
                        final Data data = buildData(KEY_TASK_OUTPUT,serialized);
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
    private Data buildData(String key, String value) {
        return new Data.Builder()
                .putString(key,value)
                .build();
    }
}