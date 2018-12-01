package afinal.app.com.finalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class PatternActivity extends AppCompatActivity {

    UserDatabaseController controller;

    String patternStr = "";
    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        controller = new UserDatabaseController(this.getBaseContext());

        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                patternStr = PatternLockUtils.patternToString(mPatternLockView, pattern);
            }

            @Override
            public void onCleared() {

            }
        });
    }

    public void saveBtn(View view){
        EditText usernamein = (EditText) findViewById(R.id.userNameIN);
        String username = usernamein.getText().toString();

        UserEntry aUser = new UserEntry();

        aUser.setUser(username);
        aUser.setPassword(patternStr);

        long inserted = controller.insertUser(aUser);

        //Log.d("DATABASE", "Insertion: "+inserted);*/

        Toast.makeText(getApplicationContext(), username +": "+ patternStr, Toast.LENGTH_SHORT).show();

        Intent myIntent = new Intent(this, LoginActivity.class);
        startActivity(myIntent);
    }
}
