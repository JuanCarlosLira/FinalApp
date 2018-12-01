package afinal.app.com.finalapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ProductDetailActivity extends AppCompatActivity {

    String id;
    String user;
    String server = "192.168.1.68";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        user = getIntent().getExtras().getString("user","");
        id = getIntent().getExtras().getString("id","1");

        Log.d("Click", ""+id);

        try{
            URL url =  new URL("http", server, 3000, "/prods/"+id );

            new DownloadProductData().execute(url);
        }catch (MalformedURLException mue){
            Log.d("CANCELLED!!" , "POOR URL");
        }

        //inId.setText(""+id);
        //inId.setText(getIntent().getStringExtra(CatalogueActivity.MESSAGE_ID));
    }

    public void updateClicked(View view){
        TextView inId = findViewById(R.id.idIn);
        EditText inName = (EditText) findViewById(R.id.nameIn);
        EditText inModel = (EditText) findViewById(R.id.modelIn);
        EditText inDesc = (EditText) findViewById(R.id.descIn);
        EditText inPrice = (EditText) findViewById(R.id.priceIn);

        ProductEntry aProd = new ProductEntry();

        aProd.setId( Integer.parseInt(""+inId.getText()) );
        aProd.setName(""+inName.getText());
        aProd.setModel(""+inModel.getText());
        aProd.setDescription(""+inDesc.getText());
        aProd.setPrice(Integer.parseInt(""+inPrice.getText()));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL("http", server, 3000, "/prods/"+inId.getText());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PATCH");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("id", aProd.getId());
            jsonParam.put("name", aProd.getName());
            jsonParam.put("model",aProd.getModel());
            jsonParam.put("description", aProd.getDescription());
            jsonParam.put("price", aProd.getPrice());

            Log.i("JSON", jsonParam.toString());
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent newActivity = new Intent( this, CatalogueActivity.class);
        newActivity.putExtra("user", user);
        startActivity(newActivity);
    }

    public void deleteClicked(View view){
        TextView inId = findViewById(R.id.idIn);
        int id = Integer.parseInt(""+inId.getText());

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

        Log.d("REMOTE DATABASE", "Deleted - "+id);

        Intent newActivity = new Intent( this, CatalogueActivity.class);
        newActivity.putExtra("user", user);
        startActivity(newActivity);
    }

    void setProgressPercent(Integer progress){

        Log.d("PROGRESS " , "We are on  " + progress + " of task" );

    }

    void showDialog(String str){

        Log.d("FINAL PROGRESS " , str );

        try{
            JSONObject json = new JSONObject(str);

            TextView inId = findViewById(R.id.idIn);
            EditText inName = (EditText) findViewById(R.id.nameIn);
            EditText inModel = (EditText) findViewById(R.id.modelIn);
            EditText inDesc = (EditText) findViewById(R.id.descIn);
            EditText inPrice = (EditText) findViewById(R.id.priceIn);

            inId.setText(json.getString("id"));
            inName.setText(json.getString("name"));
            inModel.setText(json.getString("model"));
            inDesc.setText(json.getString("description"));
            inPrice.setText(json.getString("price"));
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

        private String sendGETRequestHttpsTrustAll(URL url) { // String requestString (If have POST parameters)
            DataInputStream dis = null;
            StringBuffer messagebuffer = new StringBuffer();
            HttpURLConnection urlConnection = null;
            try {
                //("https://www.codeproject.com/");

                //Connection port HTTPS
                HttpsURLConnection urlHttpsConnection = null;

                if (url.getProtocol().toLowerCase().equals("https")) {

                    trustAllHosts(); //Trust all certificate
                    //Open Connection
                    urlHttpsConnection = (HttpsURLConnection) url.openConnection();
                    //Set Verifier
                    urlHttpsConnection.setHostnameVerifier(DO_NOT_VERIFY);
                    //Assigning value
                    urlConnection = urlHttpsConnection;

                } else {
                    urlConnection = (HttpURLConnection) url.openConnection();
                }

                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("GET"); //("POST");

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


        final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };


    }

    private static void trustAllHosts() {
        X509TrustManager easyTrustManager = new X509TrustManager() {
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{easyTrustManager};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}