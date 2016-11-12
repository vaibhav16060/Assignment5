package vaibhav.iiitd.com.assign5;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

public class MainActivity extends Activity {

    ProgressDialog mProgressDialog;
    String iiit_d_about = "https://www.iiitd.ac.in/about";
    TextView tv_data_container;
    Button btn_get_data;

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String data = savedInstanceState.getString("text_field_content");
        tv_data_container.setText(data);
        btn_get_data.setText(savedInstanceState.getString("btn_field_content"));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("text_field_content", tv_data_container.getText().toString());
        savedInstanceState.putString("btn_field_content", btn_get_data.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_data_container = (TextView)findViewById(R.id.tv_data_container);
        btn_get_data = (Button)findViewById(R.id.btn_get_data);
        btn_get_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()) {
                    new getIIITData().execute();
                    btn_get_data.setText("Showing you about IIIT-Delhi");
                }
                else{
                    showDialog();
                    btn_get_data.setText("Connect to internet and click me again !");
                    tv_data_container.setText("Internet is not connected. Try to get the internet working first !");
                }
            }
        });
    }

    void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("The network is not connected !");

        // Add the buttons
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private class getIIITData extends AsyncTask<Void, Void, Void>{

        String data;
        Document document;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("Connecting to IIIT-D");
            mProgressDialog.setMessage("Getting Data...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                document = Jsoup.connect(iiit_d_about).get();
                Elements dat = document.getElementsByTag("p");
                data = dat.get(6).text() + dat.get(7).text() + dat.get(8).text();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            tv_data_container.setText(data);
            mProgressDialog.dismiss();
            System.out.println(document.toString());
        }
    }

}
