package afinal.app.com.finalapp;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewProductActivity extends AppCompatActivity {

    String user;
    String server = "192.168.1.68";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        user = getIntent().getExtras().getString("user","");
    }

    public void addClicked(View view){
        // Read the textFields
        EditText inputName = (EditText) findViewById(R.id.nameIn);
        String name = ""+inputName.getText();
        EditText inputModel = (EditText) findViewById(R.id.modelIn);
        String model = ""+inputModel.getText();
        EditText inputDesc = (EditText) findViewById(R.id.descIn);
        String desc = ""+inputDesc.getText();
        EditText inputPrice = (EditText) findViewById(R.id.priceIn);
        String price = ""+inputPrice.getText();

        Log.d("REMOTE DATABASE", "Input Read: "+name+ " - " + model + " - " + desc +  " - " + price);

        ProductEntry aProd = new ProductEntry();

        aProd.setName(name);
        aProd.setModel(model);
        aProd.setDescription(desc);
        aProd.setPrice(Integer.parseInt(price));

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL("http", server, 3000, "/prods");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            JSONObject jsonParam = new JSONObject();
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

        // Return to the catalogue activity
        Intent newActivity = new Intent( this, CatalogueActivity.class);
        newActivity.putExtra("user", user);
        startActivity(newActivity);
    }
}
