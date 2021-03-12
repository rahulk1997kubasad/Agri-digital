package project.farmvisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chooser);
    }

    public void login(View view) {
        Button b = (Button) view;

        Intent i = new Intent(this, FarmerLoginActivity.class);

        if(b.getText().toString().equalsIgnoreCase("trader"))
            MyApplication.setUserType("trader");
        else
            MyApplication.setUserType("farmer");

        startActivity(i);

    }
}
