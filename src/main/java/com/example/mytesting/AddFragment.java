package com.example.mytesting;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static  final int PICK_IMAGE_REQUEST = 1;

    //FireStorage
    static StorageReference mStorageRef;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner typeOfClothes;
    Spinner color;
    Button selectPictures;
    ImageView image;
    Button addCloth;

    EditText nameCloth;
    Uri selImage;


    public AddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragment newInstance(String param1, String param2) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        //ID items
        typeOfClothes = root.findViewById(R.id.spinner_typeOf);
        color = root.findViewById(R.id.spinner_color);
        selectPictures = root.findViewById(R.id.selectpic);
        image = root.findViewById(R.id.imageView);
        addCloth = root.findViewById(R.id.addButton);
        nameCloth = root.findViewById(R.id.nameOfdress);



        addCloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile();
            }
        });

        selectPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);

            }
        });

        initspinnetType();
        initspinnetColor();



        return root;
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = this.getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        String fileType = typeOfClothes.getSelectedItem().toString().toLowerCase();
        String colorOfCloth = color.getSelectedItem().toString().toLowerCase();
        String nameDres = nameCloth.getText().toString().toLowerCase().replaceAll(" ", "_");

        mStorageRef = FirebaseStorage.getInstance().getReference(fileType);
        String imageName = nameDres+"."+getFileExtension(selImage);

        if (selImage != null){
            StorageReference fileReference = mStorageRef.child(imageName);

            fileReference.putFile(selImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();

                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    //createNewPost(imageUrl);
                                    FirestoreHandler.addClothe(nameDres, fileType, colorOfCloth, imageName, imageUrl);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            selImage = data.getData();
            image.setImageURI(selImage);
        }
    }

    private void initspinnetColor() {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),
                R.array.colors,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        color.setAdapter(adapter);
    }

    private void initspinnetType() {

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(getActivity(),
                R.array.typeOfClothes,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfClothes.setAdapter(adapter);


    }
}