package co.scrumfit.hwuploadimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import model.Car;
import model.Person;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    EditText edId;
    ImageView ivProfile;
    Button btnAdd, btnBrowse, btnUpload, btnFind;

    DatabaseReference personDatabase;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }

    private void initialize() {
        edId = findViewById(R.id.edId);
        ivProfile = findViewById(R.id.ivProfile);
        btnAdd = findViewById(R.id.btnAdd);
        btnBrowse = findViewById(R.id.btnBrowse);
        btnUpload = findViewById(R.id.btnUpload);
        btnFind = findViewById(R.id.btnFind);

        btnAdd.setOnClickListener(this);
        btnBrowse.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnFind.setOnClickListener(this);

        personDatabase = FirebaseDatabase
                .getInstance()
                .getReference("person");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btnAdd:
                addPerson(v);
                break;
            case R.id.btnBrowse:
                break;
            case R.id.btnUpload:
                break;
            case R.id.btnFind:
                find();
                break;

        }

    }



    private void addPerson(View v) {

        try {
            ArrayList<String> hobbies = new ArrayList<>();
            hobbies.add("Soccer");
            hobbies.add("Cooking");
            hobbies.add("Handball");
            hobbies.add("Reading");

            Car car1 = new Car("M300","Mazda","Mazda 3");

            Person p1 = new Person(300,"Richard","im1.png",car1,hobbies);

            personDatabase.child("300").setValue(p1);
            Snackbar.make(v,"The person with the id 300 is added successfully",Snackbar.LENGTH_LONG).show();
        } catch (Exception e){
            Log.d("ADV_FIREBASE",e.getMessage());
        }

    }

    private void find() {
        try {
            int id = Integer.valueOf(edId.getText().toString());

            DatabaseReference personChild =
                    FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("person")
                    .child(String.valueOf(id));

            personChild.addValueEventListener(this);
        }catch (Exception e){
            Log.d("ADV_FIREBASE", e.getMessage());
        }

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.exists()){
            String name = snapshot.child("name").getValue().toString();

            // Accessing to the sub document (list) hobbies
            ArrayList<String> hobbies = (ArrayList)snapshot.child("hobbies").getValue();
            // Accessing to the sub document Car
            HashMap car = (HashMap)snapshot.child("car").getValue();

            // Accessing to the Photo URL
            String urlPhoto = snapshot.child("photo").getValue().toString();

            Log.d("ADV_FIREBASE",car.get("brand").toString());

            Log.d("ADV_FIREBASE",hobbies.toString());

            Log.d("ADV_FIREBASE", urlPhoto.toString());

            Picasso
                    .with(this)
                    .load(urlPhoto)
                    .placeholder(R.drawable.profile2)
                    .into(ivProfile);
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}