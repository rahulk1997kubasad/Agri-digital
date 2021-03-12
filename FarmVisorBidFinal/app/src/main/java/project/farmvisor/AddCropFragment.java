package project.farmvisor;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class AddCropFragment extends Fragment {

   private EditText name,price,description,quantity;
   private static EditText bid_date;
    ImageView imageView;
    Button btn_add, btn_bid_date;
    final private int CAPTURE_IMAGE = 2;
    private String imgPath;
    private String selectedImagePath = "";
    private Uri mImageUri;
    static String lang;
    static String farmerId="";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(1000, TimeUnit.SECONDS)
            .writeTimeout(1000, TimeUnit.SECONDS)
            .readTimeout(3000, TimeUnit.SECONDS)
            .build();

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
    public ProgressDialog pDialog;
    public AddCropFragment() {
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
             bid_date.setText(""+day+"/"+(month+1)+"/"+year);
        }
    }

    public static AddCropFragment newInstance(String farmer_id, String contact, String language) {
        AddCropFragment fragment = new AddCropFragment();
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

        View root;

        if (lang.equals("English")) {
            root=inflater.inflate(R.layout.fragment_add_crop, container, false);
        }
        else
        {
            root=inflater.inflate(R.layout.kfragment_add_crop, container, false);
        }

        name=(EditText)root.findViewById(R.id.crop_name);
        price=(EditText)root.findViewById(R.id.crop_price);
        description=(EditText)root.findViewById(R.id.crop_description);
        imageView=(ImageView)root.findViewById(R.id.crop_image);
        btn_add=(Button)root.findViewById(R.id.btn_add_crop);
        quantity=(EditText)root.findViewById(R.id.crop_quantity);
        bid_date = (EditText)root.findViewById(R.id.bid_date);
        btn_bid_date = (Button)root.findViewById(R.id.btn_bid_date);
        btn_bid_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namet,pricet,descriptiont,quantityt;
                namet=name.getText().toString().trim();
                pricet=price.getText().toString().trim();
                descriptiont=description.getText().toString().trim();
                quantityt=quantity.getText().toString().trim();
                if (namet.equals("")||pricet.equals("")||descriptiont.equals("")|| quantityt.equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Feild Vacant",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    addCrop(namet,pricet,descriptiont,quantityt);
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



                intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());

                startActivityForResult(intent, 1);
            }
        });
        return root;
    }
////////////////////////CROP ADDING CODE////////////////////////////////////////
    private void addCrop(String namet, String pricet, String descriptiont, String quantityt) {
        String url=CONSTANTS.IPADDRESS+"/farmvisor/addCrops";
        Random random=new Random();
        int rand=random.nextInt();

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name",namet)
                .addFormDataPart("price",pricet)
                .addFormDataPart("description",descriptiont)
                .addFormDataPart("quantity",quantityt)
                .addFormDataPart("farmer_id",farmerId)
                .addFormDataPart("bid_date",bid_date.getText().toString())
                .addFormDataPart("image",rand +".png",
                        RequestBody.create(MEDIA_TYPE_PNG, new File(imgPath)))
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
        price.setText("");
        quantity.setText("");
        description.setText("");
        imageView.setImageResource(R.drawable.image_add_icon);
    }


//    public void callCamera(View view)
//    {
//        selectImage();
//    }
//
//    private void selectImage() {
//
//
//
//        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
//
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle("Add Photo!");
//
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//
//            @Override
//
//            public void onClick(DialogInterface dialog, int item) {
//
//                if (options[item].equals("Take Photo"))
//
//                {
//
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//
//
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
//                    //pic = f;
//
//                    startActivityForResult(intent, 1);
//
//
//
//
//
//                }
//
//                else if (options[item].equals("Choose from Gallery"))
//
//                {
//
//                    Intent intent = new Intent();
//// Show only images, no videos or anything else
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//// Always show the chooser (if there are multiple options available)
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
//
//
//
//
//
//                }
//
//                else if (options[item].equals("Cancel")) {
//
//                    dialog.dismiss();
//
//                }
//
//            }
//
//        });
//
//        builder.show();
//
//    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_take__photo, menu);
//        return true;
//    }


    public Uri setImageUri() {
        // Store image in dcim
        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }


    public String getImagePath() {
        return imgPath;
    }


    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 250;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                selectedImagePath = getImagePath();
                imageView.setImageBitmap(decodeFile(selectedImagePath));
                try{
                    imgPath=selectedImagePath;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (requestCode == 2) {

                mImageUri = data.getData();
                System.out.println("21845"+mImageUri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), mImageUri);
                    // Log.d(TAG, String.valueOf(bitmap));


                    imageView.setImageBitmap(bitmap);
                    String imgPathl = getFilePath(getActivity().getApplicationContext(),mImageUri);
                    imgPath=imgPathl;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

    }


    public static String getFilePath(Context context, Uri uri)
    {
        int currentApiVersion;
        try
        {
            currentApiVersion = android.os.Build.VERSION.SDK_INT;
        }
        catch(NumberFormatException e)
        {
            //API 3 will crash if SDK_INT is called
            currentApiVersion = 3;
        }
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst())
            {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
        else if (currentApiVersion <= Build.VERSION_CODES.HONEYCOMB_MR2 && currentApiVersion >= Build.VERSION_CODES.HONEYCOMB)

        {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;

            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();

            if (cursor != null)
            {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }
        else
        {

            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }

}
