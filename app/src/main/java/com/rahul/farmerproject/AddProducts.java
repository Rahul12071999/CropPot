package com.rahul.farmerproject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.time.Instant;

public class AddProducts extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST=1;
    private DatabaseReference db;
    private StorageReference ref;
    private Uri mImageuri;
    EditText name,wt;
    ImageView im;
    ProgressBar pb;
    private StorageTask muploadtask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        db= FirebaseDatabase.getInstance().getReference("Farmer/7020139865/Products");
        //tv=findViewById(R.id.textView);
       name=findViewById(R.id.editText3);
       wt=findViewById(R.id.editText);
       im=findViewById(R.id.imageView);
       pb=findViewById(R.id.progressBar);
       ref= FirebaseStorage.getInstance().getReference("Uploads");
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));

    }
    public void uplodclick(View view){
//        //String ProdName=tv.getText().toString().trim();
//        String Value=et.getText().toString().trim();
//        db.child(ProdName).setValue(Value);
        if(muploadtask!=null && muploadtask.isInProgress()){
            Toast.makeText(AddProducts.this,"Upload in Progress",Toast.LENGTH_SHORT).show();
        }
        else
        if(mImageuri!=null){
            StorageReference fileref=ref.child(System.currentTimeMillis()+"."+getFileExtension(mImageuri));
            muploadtask=fileref.putFile(mImageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            pb.setProgress(0);
                            Toast.makeText(AddProducts.this,"Upload Success",Toast.LENGTH_LONG).show();
                            /*uploads upload=new uploads(name.getText().toString().trim(),
                                    taskSnapshot.getUploadSessionUri().toString(),wt.getText().toString().trim());
                            String uploadid=db.push().getKey();
                            db.child(uploadid).setValue(upload);*/
                            Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri DownloadUrl=urlTask.getResult();
                            uploads upload=new uploads(name.getText().toString().trim(),DownloadUrl.toString().trim(),wt.getText().toString().trim());
                            String uploadId = db.push().getKey();
                            db.child(uploadId).setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddProducts.this,"Upload Failed",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress=(int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            pb.setProgress(progress);

                        }
                    });
        }else {
            Toast.makeText(AddProducts.this ,"File is not selected", Toast.LENGTH_SHORT).show();
        }

    }
    public void choosebtn(View view){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            mImageuri=data.getData();
            im.setImageURI(mImageuri);
        }
    }
}
