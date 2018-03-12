package com.example.ram.benellacompat.admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ram.benellacompat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mindorks.paracamera.Camera;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView upload, upload1, upload2, upload3, upload4, upload5;
    EditText mrp, from, to, description;
    FirebaseStorage storage;
    DatabaseReference mRoot = FirebaseDatabase.getInstance().getReference();
    Button post;
    Camera firstCamera;
    public static final int RequestPermissionCode = 1;
    ArrayList<String> bitmapArrayList = new ArrayList<>();
    ProgressDialog dialog;
    StorageReference storageRef;
    Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        storage = FirebaseStorage.getInstance();
        mHandler=new Handler();
        storageRef = storage.getReference();
        upload = findViewById(R.id.upload);
        upload1 = findViewById(R.id.upload1);
        upload2 = findViewById(R.id.upload2);
        upload3 = findViewById(R.id.upload3);
        upload4 = findViewById(R.id.upload4);
        upload5 = findViewById(R.id.upload5);
        upload.setOnClickListener(this);
        upload1.setOnClickListener(this);
        upload2.setOnClickListener(this);
        upload3.setOnClickListener(this);
        upload4.setOnClickListener(this);
        upload5.setOnClickListener(this);
        mrp = findViewById(R.id.mrp);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        description = findViewById(R.id.description);
        post = findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.setScaleX(.8f);
                post.setScaleY(.8f);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        post.setScaleX(1);
                        post.setScaleY(1);
                    }
                }, 100);
                dialog.setMessage("Posting..");
                dialog.show();
                sendtoFirebase();
            }
        });
        EnableRuntimePermissionToAccessCamera();

    }

    private void sendtoFirebase() {
        if(bitmapArrayList.isEmpty()){
            Toast.makeText(this, "upload some images", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if(mrp.getText().toString().isEmpty()){
            mrp.setError("fill mrp");
            Toast.makeText(this, "fill MRP price", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            return;
        }
        if(from.getText().toString().isEmpty()){
            from.setError("fill from");
            dialog.dismiss();
            Toast.makeText(this, "fill from detail.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(to.getText().toString().isEmpty()){
            to.setError("fill To");
            dialog.dismiss();
            Toast.makeText(this, "fill to detail", Toast.LENGTH_SHORT).show();
            return;
        }
        if(description.getText().toString().isEmpty()){
            description.setError("fill description.");
            dialog.dismiss();
            Toast.makeText(this, "fill Description", Toast.LENGTH_SHORT).show();
            return;
        }
        String mMrp = mrp.getText().toString();
        String mFrom = from.getText().toString();
        String mTo = to.getText().toString();
        String mdescription = description.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("mrp", mMrp);
        map.put("from", mFrom);
        map.put("to", mTo);
        map.put("description", mdescription);
        map.put("images",bitmapArrayList);
        mRoot.child("ADMIN_POST").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(AdminActivity.this, "something went wrong try again.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AdminActivity.this, "Successfully Posted", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    dialog.dismiss();
                }

            }
        });
    }

    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AdminActivity.this,
                Manifest.permission.CAMERA) &&
                ActivityCompat.shouldShowRequestPermissionRationale(AdminActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Printing toast message after enabling runtime permission.
            Toast.makeText(this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Bitmap bitmap = firstCamera.getCameraBitmap();
                if (bitmap != null) {
                    bitmapArrayList.add("preview1.jpg");
                    uploadbitmap(bitmap,"preview1.jpg", upload);

                } else {
                    Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                Bitmap bitmap1 = firstCamera.getCameraBitmap();
                if (bitmap1 != null) {
                    uploadbitmap(bitmap1,"preview2.jpg", upload1);
                    bitmapArrayList.add("preview2.jpg");
                } else {
                    Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                Bitmap bitmap2 = firstCamera.getCameraBitmap();
                if (bitmap2 != null) {
                    uploadbitmap(bitmap2,"preview3.jpg", upload2);
                    bitmapArrayList.add("preview3.jpg");
                } else {
                    Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                Bitmap bitmap3 = firstCamera.getCameraBitmap();
                if (bitmap3 != null) {
                    uploadbitmap(bitmap3,"preview4.jpg", upload3);
                    bitmapArrayList.add("preview4.jpg");
                } else {
                    Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 5:
                Bitmap bitmap4 = firstCamera.getCameraBitmap();
                if (bitmap4 != null) {
                    uploadbitmap(bitmap4,"preview5.jpg", upload4);
                    bitmapArrayList.add("preview5.jpg");
                } else {
                    Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
                break;
            case 6:
                Bitmap bitmap5 = firstCamera.getCameraBitmap();
                if (bitmap5 != null) {
                    uploadbitmap(bitmap5,"preview6.jpg",upload5);
                    bitmapArrayList.add("preview6.jpg");
                } else {
                    Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.upload:
                firstCamera = new Camera.Builder()
                        .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                        .setTakePhotoRequestCode(1)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        .build(this);
                try {
                    firstCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.upload1:
                firstCamera = new Camera.Builder()
                        .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                        .setTakePhotoRequestCode(2)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75).build(this);
                try {
                    firstCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.upload2:
                firstCamera = new Camera.Builder()
                        .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                        .setTakePhotoRequestCode(3)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        // it will try to achieve this height as close as possible maintaining the aspect ratio;
                        .build(this);
                try {
                    firstCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.upload3:
                firstCamera = new Camera.Builder()
                        .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                        .setTakePhotoRequestCode(4)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        // it will try to achieve this height as close as possible maintaining the aspect ratio;
                        .build(this);
                try {
                    firstCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.upload4:
                firstCamera = new Camera.Builder()
                        .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                        .setTakePhotoRequestCode(5)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        // it will try to achieve this height as close as possible maintaining the aspect ratio;
                        .build(this);
                try {
                    firstCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.upload5:
                firstCamera = new Camera.Builder()
                        .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                        .setTakePhotoRequestCode(6)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        // it will try to achieve this height as close as possible maintaining the aspect ratio;
                        .build(this);
                try {
                    firstCamera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void uploadbitmap(final Bitmap bitmap, String name, final ImageView imgView){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[] stream=byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(stream);
        StorageReference mstorageref=storageRef.child("images").child(name);
        UploadTask uploadTask=mstorageref.putStream(byteArrayInputStream);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                if (dialog.isShowing()) {

                } else {
                    dialog.setMessage("Uploading..");
                    dialog.show();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("Download", downloadUrl.toString());
                Toast.makeText(AdminActivity.this, "image upload successful", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                String path = MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(), bitmap, "Title", null);
                Picasso.with(getApplicationContext()).load(Uri.parse(path)).resize(120,120).into(imgView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Log.d("TAG","failed upload");
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(firstCamera!=null){
            firstCamera.deleteImage();
        }

    }
}
