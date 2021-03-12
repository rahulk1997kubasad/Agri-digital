package project.farmvisor;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CropRequestFragment extends Fragment {
    private EditText name,description,quantity;
    Button btn_add;
    static String farmerId,lang;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS)
            .writeTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(3000, TimeUnit.SECONDS)
            .build();
    public CropRequestFragment() {
        // Required empty public constructor
    }


    public static CropRequestFragment newInstance(String farmer_id, String contact, String language) {
        CropRequestFragment fragment = new CropRequestFragment();
        farmerId=farmer_id;
        lang=language;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root;

        if (lang.equals("English")) {
            root= inflater.inflate(R.layout.fragment_crop_request, container, false);
        }
        else
        {
            root= inflater.inflate(R.layout.kfragment_crop_request, container, false);
        }

        name=(EditText)root.findViewById(R.id.crop_name);
        description=(EditText)root.findViewById(R.id.crop_description);
        btn_add=(Button)root.findViewById(R.id.btn_add_crop);
        quantity=(EditText)root.findViewById(R.id.crop_quantity);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namet,descriptiont,quantityt;
                namet=name.getText().toString().trim();
                descriptiont=description.getText().toString().trim();
                quantityt=quantity.getText().toString().trim();
                if (namet.equals("")||descriptiont.equals("")|| quantityt.equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Feild Vacant",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    addCrop(namet,descriptiont,quantityt);
                }
            }
        });
        return root;
    }

    private void addCrop(String namet, String descriptiont, String quantityt) {
        String url=CONSTANTS.IPADDRESS+"/farmvisor/addCropRequest";
        Random random=new Random();
        int rand=random.nextInt();

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name",namet)
                .addFormDataPart("description",descriptiont)
                .addFormDataPart("quantity",quantityt)
                .addFormDataPart("farmer_id",farmerId)
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Toast.makeText(CameraActivity.this,response.toString(),Toast.LENGTH_SHORT);
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);



                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    Toast.makeText(CameraActivity.this,responseHeaders.name(i) + ": " + responseHeaders.value(i),Toast.LENGTH_SHORT).show();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        updateUI();
                    }
                });


            }
        });
    }

    private void updateUI() {
        name.setText("");
        quantity.setText("");
        description.setText("");
    }


}
