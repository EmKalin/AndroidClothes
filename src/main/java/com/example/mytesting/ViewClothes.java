package com.example.mytesting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewClothes extends AppCompatActivity implements ImageAdapter.OnItemClickListner {

    private RecyclerView mRecylerView;
    private ImageAdapter mAdapter;

    private List<Cloth> mUploadCloth;
    private List<UploadImage> mUploadImages;

    //static FirebaseFirestore mStorageReferance;
    static FirebaseStorage  mStorageReferance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clothes);


        mRecylerView = findViewById(R.id.recycle_view);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String fileTypeToLoad = intent.getStringExtra("typeOf");

        mUploadImages = new ArrayList<>();
        mUploadCloth = new ArrayList<>();
        FirestoreHandler.getCloth(fileTypeToLoad, new OnCompleteListener<QuerySnapshot>() {
            static final String TAG = "Downloading";
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Cloth cloth = document.toObject(Cloth.class);
                        Log.d(TAG, cloth.toString());
                        mUploadCloth.add(cloth);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        for(Cloth clos: mUploadCloth){
                            UploadImage upIm = new UploadImage(document.getId(), clos.getName(), clos.getImageUrl());
                            mUploadImages.add(upIm);
                        }
                        mAdapter = new ImageAdapter(ViewClothes.this, mUploadImages);
                        mRecylerView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickListner(ViewClothes.this);
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Normal Click at position: "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        UploadImage selestIm = mUploadImages.get(position);
        String  selKey = selestIm.mKey;

        Toast.makeText(this, "Delete at position: "+selestIm.mImageUrl, Toast.LENGTH_LONG).show();
        Log.d("Deleting", selestIm.mImageUrl);


        StorageReference imref = FirebaseStorage.getInstance().getReferenceFromUrl(selestIm.mImageUrl);
        Log.d("Deleting", String.valueOf(imref));
        imref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("clothes").document(selKey).delete();

            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}