package afinal.app.com.finalapp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class CatalogueActivity extends AppCompatActivity {

    public static String CHANNEL_ID = "Notification Chanel #1";

    String user;
    String server = "192.168.1.68";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        overridePendingTransition(R.anim.translate_left, R.anim.translate_right);

        user = getIntent().getExtras().getString("user","");

        createNotificationChannel();

        //Toast.makeText(getApplicationContext(), user, Toast.LENGTH_SHORT).show();
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome: "+user);

        loadWebFile();
    }

    void loadWebFile(){

        try{
            URL url =  new URL("http", server, 3000, "/prods" );

            new DownloadProductData().execute(url);
        }catch (MalformedURLException mue){
            Log.d("CANCELLED!!" , "POOR URL");
        }

    }

    public void removeClicked(View view){
        int id = ((View)view.getParent()).getId();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL("http", server, 3000, "/prods/"+id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        doNotification("Deleted Item", "Item No: "+id,"Item No: "+id);

        Log.d("REMOTE DATABASE", "Deleted - "+id);

        loadWebFile();
    }

    public void editClicked(View view){
        Intent newActivity = new Intent( this, ProductDetailActivity.class);

        String value = ""+((View)view.getParent()).getId();

        newActivity.putExtra("id", value);
        newActivity.putExtra("user", user);
        startActivity(newActivity);
    }

    public void addItemClicked(View view){
        Intent newActivity = new Intent( this, NewProductActivity.class);
        newActivity.putExtra("user", user);
        startActivity(newActivity);
    }

    void setProgressPercent(Integer progress){

        Log.d("PROGRESS " , "We are on  " + progress + " of task" );

    }

    void showDialog(String str){

        Log.d("FINAL PROGRESS " , str );

        //ViewGroup tableGroup = (ViewGroup)findViewById(R.id.table_layout_users);

        try{
            //JSONObject json = new JSONObject(str);
            //JSONArray map = (JSONArray) json.get("results");
            JSONArray map = new JSONArray(str);

            ViewGroup table = findViewById(R.id.remoteItemsTable);
            table.removeAllViews();

            for(int i = 0; i < map.length() ; i++){

                JSONObject cursor = (JSONObject) map.get(i);

                Log.d("RESULTS " , "-><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + cursor.getString("name") );

                Log.d("REGISTRY" , cursor.getString( "name"));

                View row = getLayoutInflater().inflate(R.layout.prod_row, null);
                TextView prodName = row.findViewById(R.id.prodName);
                TextView prodModel = row.findViewById(R.id.prodModel);

                prodName.setText(cursor.getString("name"));
                prodModel.setText(cursor.getString("model"));

                row.setId(cursor.getInt("id"));
                table.addView(row);
                //TextView textView = (TextView)instance.findViewById(R.id.userNameView);
                //textView.setText( cursor.getString("name"));
                //tableGroup.addView(instance);

            }
        }catch (JSONException jse){
            Log.e("Json","Json Error " + jse);
        }
    }

    class DownloadProductData extends AsyncTask<URL, Integer, Long> {

        String finalString = "";

        protected void onPreExecute(){
            Log.d("PROGRESS EXECUTE", "Starting");
        }

        protected Long doInBackground(URL... urls) {

            try{

                if( !isCancelled()){

                    int count = urls.length;
                    long totalSize = 0;

                    URL url = urls[0];

                    finalString = sendGETRequestHttp(url);
                    //finalString = sendGETRequestHttpsTrustAll(url);

                    Log.d("FINAL STRING DONE","READY");
                    //Log.d("FINAL STRING DONE HTTP REQUEST", finalString);

                    publishProgress(new Integer(100));
                    return totalSize;
                }

            }catch (Exception ioe){

                Log.d("ERROR","ERROR ON LOADING " + ioe);
            }
            return Long.valueOf(0);
        }

        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            showDialog(finalString);
        }


        private String sendGETRequestHttp(URL url) { // String requestString (If have POST parameters)
            DataInputStream dis = null;
            StringBuffer messagebuffer = new StringBuffer();
            HttpURLConnection urlConnection = null;
            try {
                //URL url = new URL("http://www.yoururl.com/"); //Simple HttpURLConnection request

                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("GET");

                //OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                //out.write(requestString.getBytes());
                //out.flush();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                dis = new DataInputStream(in);
                int ch;
                long len = urlConnection.getContentLength();

                if (len != -1) {
                    for (int i = 0; i < len; i++)
                        if ((ch = dis.read()) != -1) {
                            messagebuffer.append((char) ch);
                        }
                } else {
                    while ((ch = dis.read()) != -1)
                        messagebuffer.append((char) ch);
                }
                dis.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return messagebuffer.toString();
        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    void doNotification(String title, String text, String bigText){

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(bigText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1000, mBuilder.build());

    }
}
