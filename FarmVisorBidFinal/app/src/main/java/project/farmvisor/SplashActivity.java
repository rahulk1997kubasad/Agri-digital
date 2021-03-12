package project.farmvisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.SQLException;

public class SplashActivity extends AppCompatActivity {
    DBHelper dbManager;
    String language="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        dbManager= new DBHelper(this);
        addLanguage("English");
        final Thread t = new Thread(){

            public void run()
            {
                try
                {
                    sleep(3000);

                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    Intent openActivity = new Intent(getApplicationContext(),UserChooserActivity.class);
                    openActivity.putExtra("language",language);
                    startActivity(openActivity);
                    finish();
                }

            }

        };
        t.start();
    }

    private void addLanguage(String english) {
        try {
            language=dbManager.getLanguage(1);
            if (language.equals(""))
            {
                if (dbManager.addLanguage(english))
                {
                    language=english;            }
                else
                {
                    recreate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (dbManager.addLanguage(english))
            {
                language=english;            }
            else
            {
//                recreate();
            }
        }

    }
}
