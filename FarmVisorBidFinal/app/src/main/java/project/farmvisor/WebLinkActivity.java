package project.farmvisor;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WebLinkActivity extends AppCompatActivity {

    List<WebLinkObject> webLinkObjects ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_link);
        getWebSiteList();

        shoLinks();
    }

    private void shoLinks() {
        DBHelper dbHelper = new DBHelper(this);
        String language = "";

        try {
            language = dbHelper.getLanguage(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LinearLayout layout = (LinearLayout)findViewById(R.id.weblinkList);

        layout.removeAllViews();

        for (final WebLinkObject webLinkObject : webLinkObjects)
        {

            View child = getLayoutInflater().inflate(R.layout.snippet_weblinks, null);
            TextView webLink = child.findViewById(R.id.webLink);
            webLink.setText(webLinkObject.getEngString());
            if (!"English".equals(language))
            {
                webLink.setText(webLinkObject.getKanString());
            }
            webLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webLinkObject.getWebLink()));
                    startActivity(browserIntent);
                }
            });
            layout.addView(child);
        }

    }

    private void getWebSiteList() {
        webLinkObjects = new ArrayList<>();

        webLinkObjects.add(new WebLinkObject("http://www.indiaagristat.com/","ಕೃಷಿ ಅಂಕಿಅಂಶ","Agriculture Statistic"));
        webLinkObjects.add(new WebLinkObject("http://agrixchange.net/","ಕೃಷಿ ವಿನಿಮಯ","Agriculture exchange"));
        webLinkObjects.add(new WebLinkObject("http://agricoop.nic.in/","ಕೃಷಿ ಮತ್ತು ನಿಗಮ","Agriculture and Corporation"));
        webLinkObjects.add(new WebLinkObject("http://dahd.nic.in/","ಪಶುಸಂಗೋಪನೆ","Animal Husbandry"));

        //Add webLinkObject as above
        //1. Weblink
        //2. Display text in Kannada
        //3. Display text in English
    }
}
