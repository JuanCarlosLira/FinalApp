package afinal.app.com.finalapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class PatternActivity2 extends AppCompatActivity {

    String user;
    String password;

    String patternStr = "";
    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern2);

        user = getIntent().getExtras().getString("user","");
        password = getIntent().getExtras().getString("password","");

        //Toast.makeText(getApplicationContext(), user+": " +password, Toast.LENGTH_SHORT).show();

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

                if (patternStr.equals(password)){
                    goToCatalogue();
                    //Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                } else {
                    mPatternLockView.clearPattern();
                    Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCleared() {

            }
        });
    }

    public void goToCatalogue(){
        Intent myIntent = new Intent(this, CatalogueActivity.class);
        myIntent.putExtra("user", user);
        startActivity(myIntent);
    }
}
