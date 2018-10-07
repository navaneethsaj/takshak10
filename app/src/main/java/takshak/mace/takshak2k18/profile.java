package takshak.mace.takshak2k18;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class profile extends AppCompatActivity {
    TextView name,phone,rank,notificationBox;
    boolean ismessageexpanded = false;
    String MyPREFERENCES = "savedNotification";
    SharedPreferences.Editor editor;

    String url = "https://us-central1-takshakapp18.cloudfunctions.net/getnotification";
    SharedPreferences sharedpreferences;

    AlertDialog dialog;
    AlertDialog.Builder alertBuilder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Notifications");
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        notificationBox = findViewById(R.id.notificationBox);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        String msg = sharedpreferences.getString("message","");
        notificationBox.setText(msg);

        alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Syncing");
        alertBuilder.setMessage("Fetching latest updates ...");
        dialog = alertBuilder.create();
        //Execute only when internet connection is LIVE
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // notify user you are online
            Log.d("connectivity","Connected");
            if (!dialog.isShowing()){
                dialog.show();
            }
            new MyAsyncTask().execute(url);

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this,"No Network\nNotification outdated",Toast.LENGTH_LONG).show();
        }
    }
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(4, TimeUnit.SECONDS)
                    .writeTimeout(4,TimeUnit.SECONDS)
                    .connectTimeout(4,TimeUnit.SECONDS).build();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                Log.d("body","ok");
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("request","timedout");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            super.onPostExecute(s);
            if (s != null){
                Log.d("resp","not null");
                notificationBox.setVisibility(View.VISIBLE);
                notificationBox.setText(s);
                editor.putString("message",s);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),"Network Slow",Toast.LENGTH_SHORT).show();
                //notificationBox.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
