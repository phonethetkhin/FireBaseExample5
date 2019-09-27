package com.example.firebaseexample5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
private Button btnChooseImage,btnUpload;
private ImageView imgPhoto;
private Uri FilePath;
private final int PICK_IMAGE_REQUEST=1;
FirebaseDatabase database;
DatabaseReference databaseReference;
FirebaseStorage storage;
StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnChooseImage=findViewById(R.id.btnChooseImage);
        btnUpload=findViewById(R.id.btnUpload);
        imgPhoto=findViewById(R.id.imgPhoto);
        storage=FirebaseStorage.getInstance();
        reference=storage.getReference();

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ChooseImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });

    }
    private void ChooseImage()
    {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data!=null && data.getData()!=null)
        {
            FilePath=data.getData();
            Bitmap bitmap= null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),FilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgPhoto.setImageBitmap(bitmap);
        }
    }
    private void UploadImage()
    {
if(FilePath!=null)
{
    final ProgressDialog dialog=new ProgressDialog(this);
    dialog.setTitle("Uploading....");
    dialog.show();
    final StorageReference ref=reference.child("images/"+ UUID.randomUUID().toString());
    ref.putFile(FilePath)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {// this is Upload Task
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Upload successed. At first, we get Url immediately. But DownloadUrl task is not complete. So, let's say url is null that's why
                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {//Here We add download Task onCompleteListener
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            //We are sure here download task is completed
                            if(task.isSuccessful()){
                                Uri imageUrl = task.getResult();
                                database=FirebaseDatabase.getInstance();
                                databaseReference=database.getReference("PhotoModel");
                                String ModelID=databaseReference.push().getKey();
                                ClothModel cmodel=new ClothModel(001,2000,"Ladies Wear","Large",imageUrl.toString(),"Red");
                                databaseReference.child(ModelID).setValue(cmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "Both task completed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            dialog.dismiss();
                        }
                    });

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Upload Failed!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount();
                    dialog.setMessage("Uploaded "+(int)progress+" %");
                }
            });
}


    }

}
