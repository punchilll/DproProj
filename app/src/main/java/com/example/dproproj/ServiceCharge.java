package com.example.dproproj;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.example.dproproj.Retrofit.INodeJS;
import com.example.dproproj.Retrofit.RetrofitClient;
import com.example.dproproj.utils.CountingRequestBody;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class ServiceCharge extends AppCompatActivity {

    private static final String TAG = "ServiceCharge";
    EditText path;
    Button selectImage,btn_upload;
    private static final int READ_REQUEST_CODE = 42;
    ImageView imagePreview;
    Uri uri;

    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText service_charge,note;
    Spinner size;

    private Button btn_service_charge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_charge);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {/* ... */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken Token) {/* ... */}
                }).check();

        //API
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        btn_service_charge = (Button) findViewById(R.id.btn_service_charge);
        size = (Spinner) findViewById(R.id.size);
        service_charge = (EditText) findViewById(R.id.charge);
        note = (EditText) findViewById(R.id.note);

        path = (EditText) findViewById(R.id.path);
        //btn_upload = (Button) findViewById(R.id.btn_upload);
        selectImage = (Button)findViewById(R.id.selectImage);
        imagePreview = (ImageView)findViewById(R.id.imagePreview);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();
            }
        });

        btn_service_charge.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                uploadImage(size.getSelectedItem().toString(), service_charge.getText().toString(), note.getText().toString());
            }
        });

    }

    public void serviceCharge(String size, String service_charge, String note, String pic_evidence){
        compositeDisposable.add(myAPI.serviceCharge(size,service_charge,note,pic_evidence)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(ServiceCharge.this, " "+s, Toast.LENGTH_SHORT).show();
                        openServiceCharge();
                    }
                })
        );
    }

    public void openServiceCharge(){
        Intent intent = new Intent(this, ServiceCharge.class);
        startActivity(intent);
    }

    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                imagePreview.setImageURI(uri);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String uriToFilename(Uri uri) {
        String path = null;

        if ((Build.VERSION.SDK_INT < 19) && (Build.VERSION.SDK_INT > 11)) {
            path = getRealPathFromURI_API11to18(this, uri);
        } else {
            path = getFilePath(this, uri);
        }

        return path;
    }

    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;
        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getFilePath(Context context, Uri uri) {
        //Log.e("uri", uri.getPath());
        String filePath = "";
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);
            //Log.e("wholeID", wholeID);
            // Split at colon, use second item in the array
            String[] splits = wholeID.split(":");
            if (splits.length == 2) {
                String id = splits[1];

                String[] column = {MediaStore.Images.Media.DATA};
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }
        } else {
            filePath = uri.getPath();
        }
        return filePath;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadImage(String size, String service_charge, String note){

        if(uri == null){
            return;
        }

        final File imageFile = new File(uriToFilename(uri));
        Uri uris = Uri.fromFile(imageFile);
        String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uris.toString());
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        String imageName = imageFile.getName();

        serviceCharge(size, service_charge, note, imageName);
        //Log.e(TAG, imageFile.getName()+" "+mime+" "+uriToFilename(uri));
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", imageName,
                        RequestBody.create(imageFile, MediaType.parse(mime)))
                .build();

        final CountingRequestBody.Listener progressListener = new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long bytesRead, long contentLength) {

            }
        };

        OkHttpClient imageUploadClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        if (originalRequest.body() == null) {
                            return chain.proceed(originalRequest);
                        }
                        Request progressRequest = originalRequest.newBuilder()
                                .method(originalRequest.method(),
                                        new CountingRequestBody(originalRequest.body(), progressListener))
                                .build();

                        return chain.proceed(progressRequest);

                    }
                })
                .build();
        Request request = new Request.Builder()
                //.url("http://172.20.10.2/uploadToServer/uploadFile.php")
                .url("http://192.168.1.37/uploadToServer/uploadFile.php")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .post(requestBody)
                .build();


        imageUploadClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                //Toast.makeText(ChatScreen.this, "Error uploading file", Toast.LENGTH_LONG).show();
                Log.e("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String mMessage = response.body().string();

                ServiceCharge.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, mMessage);
                    }
                });
            }
        });

        openFirstPage();
    }

    public void openFirstPage(){
        Intent intent = new Intent(this, FirstPage.class);
        startActivity(intent);
    }
}