package afinal.app.com.finalapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    UserDatabaseController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        controller = new UserDatabaseController(this.getBaseContext());
    }

    public void register(View view){
        Intent myIntent = new Intent(this, PatternActivity.class);
        startActivity(myIntent);
    }

    public void logIn(View view){
        EditText inputUser = (EditText) findViewById(R.id.editText);
        String user = ""+inputUser.getText();

        //String[] columns = new String[]{UserEntry.COLUMN_USER+"", UserEntry.COLUMN_PSWD+""};
        Cursor cursor = controller.selectUsers(UserEntry.COLUMN_USER+" = \"" + user +"\"" , null);

        //Toast.makeText(getApplicationContext(), UserEntry.COLUMN_USER+" = \"" + user +"\"", Toast.LENGTH_SHORT).show();

        if (cursor.moveToNext()){
            String myStr = cursor.getString(cursor.getColumnIndex("user")) + ": " + cursor.getString(cursor.getColumnIndex("password"));
            Toast.makeText(getApplicationContext(), myStr, Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(this, PatternActivity2.class);
            myIntent.putExtra("user", cursor.getString(cursor.getColumnIndex("user")));
            myIntent.putExtra("password", cursor.getString(cursor.getColumnIndex("password")));
            startActivity(myIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
        }
    }
}
