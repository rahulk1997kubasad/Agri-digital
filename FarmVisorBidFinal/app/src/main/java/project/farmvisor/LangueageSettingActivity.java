package project.farmvisor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LangueageSettingActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    DBHelper dbManager;
    Button btn_click;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langueage_setting);
        radioGroup=(RadioGroup)findViewById(R.id.languageGroup);
        btn_click=(Button)findViewById(R.id.btn_click);

        dbManager=new DBHelper(this);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.radioKannnada)
                {
                    if (dbManager.update("Kannada"))
                    {
                        btn_click.setText("ಭಾಷೆ ಹೊಂದಿಸಿ");
                        Toast.makeText(getApplicationContext(),"ನೀವು ಕನ್ನಡ ಭಾಷೆಯನ್ನು ಆಯ್ಕೆ ಮಾಡಿದ್ದೀರಿ",Toast.LENGTH_SHORT).show();           }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Something went wrong please try again later!",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (i==R.id.radioEnglish){

                    if (dbManager.update("English"))
                    {
                        btn_click.setText("Set Language");
                        Toast.makeText(getApplicationContext(),"You just selected English language",Toast.LENGTH_SHORT).show();           }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Something went wrong please try again later!",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FarmerLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(getApplicationContext(),FarmerLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
