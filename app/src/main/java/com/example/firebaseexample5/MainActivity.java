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
private Button btnChooseImage1,btnChooseImage2,btnChooseImage3,btnChooseImage4,btnChooseImage5,btnUpload;
private ImageView imgPhoto1,imgPhoto2,imgPhoto3,imgPhoto4,imgPhoto5;
private Uri FilePath;
private int requestcode=0;
FirebaseDatabase database;
DatabaseReference databaseReference;
FirebaseStorage storage;
StorageReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnChooseImage1=findViewById(R.id.btnChooseImage1);
        btnChooseImage2=findViewById(R.id.btnChooseImage2);
        btnChooseImage3=findViewById(R.id.btnChooseImage3);
        btnChooseImage4=findViewById(R.id.btnChooseImage4);
        btnChooseImage5=findViewById(R.id.btnChooseImage5);
        btnUpload=findViewById(R.id.btnUpload);
        imgPhoto1=findViewById(R.id.imgPhoto1);
        imgPhoto2=findViewById(R.id.imgPhoto2);
        imgPhoto3=findViewById(R.id.imgPhoto3);
        imgPhoto4=findViewById(R.id.imgPhoto4);
        imgPhoto5=findViewById(R.id.imgPhoto5);
        storage=FirebaseStorage.getInstance();
        reference=storage.getReference();

        btnChooseImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            ChooseImage(1);
            }
        });
        btnChooseImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage(2);
            }
        });
        btnChooseImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage(3);
            }
        });
        btnChooseImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage(4);
            }
        });
        btnChooseImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage(5);
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  UploadImage();
            }
        });

    }
    private void ChooseImage(int requestcode)
    {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Picture"),requestcode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK && data!=null && data.getData()!=null)
        {
            FilePath=data.getData();
            Bitmap bitmap= null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),FilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            switch (requestCode) {
                case 1:
                    imgPhoto1.setImageBitmap(bitmap);
                    break;
                case 2:
                    imgPhoto2.setImageBitmap(bitmap);
                    break;
                case 3:
                    imgPhoto3.setImageBitmap(bitmap);
                    break;
                case 4:
                    imgPhoto4.setImageBitmap(bitmap);
                    break;

                case 5:
                    imgPhoto5.setImageBitmap(bitmap);
                    break;

            }
        }
    }
   /* private void UploadImage()
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
                                for(int i=0;i<=5;i++) {
                                    String[] imagearray = imageUrl.toString();
                                }
                                database=FirebaseDatabase.getInstance();
                                databaseReference=database.getReference("PhotoModel");
                                String ModelID=databaseReference.push().getKey();
                                ClothModel cmodel=new ClothModel(001,2000,"Ladies Wear","Large","Red",imageUrl.toString());
                                databaseReference.child(ModelID).setValue(cmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(MainActivity.this, "Upload completed", Toast.LENGTH_SHORT).show();

                                    }

                                });

                            }
                            imgPhoto1.setImageResource(android.R.color.transparent);

                            Toast.makeText(MainActivity.this, "Choose Another Photo", Toast.LENGTH_LONG).show();
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
*/

}
