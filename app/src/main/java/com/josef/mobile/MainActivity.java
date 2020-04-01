package com.josef.mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.josef.josefmobile.R;

import static com.josef.mobile.Config.HOME_ACTIVITY;

public class MainActivity extends AppCompatActivity {

    private final Echo echo = new Echo();

    private TextView buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonView = findViewById(R.id.buttonview);
        buttonView.setText(HOME_ACTIVITY);
    }

    public void performPresenterActivity(View view) {
        Intent intent = new Intent(this, PresenterActivity.class);
        startActivity(intent);
    }

    /** private class EndPointsAsync extends AsyncTask<Void, Void, Message> {
        @Override
        protected Message doInBackground(Void... voids) {
            Message message = new Message();
            message.setMessage("echo me back..");
            return echo.echo(message, null);
        }

        @Override
        protected void onPostExecute(@Nullable Message result) {
            buttonView.setText(result.getMessage());
        }
    }**/
}
