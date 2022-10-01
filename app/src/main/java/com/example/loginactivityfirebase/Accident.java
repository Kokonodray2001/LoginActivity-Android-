package com.example.loginactivityfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Accident extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);
        Button fire =  findViewById(R.id.firebtn);
        Button see =  findViewById(R.id.seebtn);
        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store(25.646464,85.55616161);
            }

        });
        see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAccidentWithInRange(1.222,5.5555);
            }
        });
    }

    private void checkAccidentWithInRange(Double  lng, Double lat) {
        ArrayList<Pair<Double, Double>> arrayList =  new ArrayList<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Date date = Calendar.getInstance().getTime();
        String year = String.valueOf(date.getYear());
        String month =  String.valueOf(date.getMonth());
        String day =  String.valueOf(date.getDate());
        int hr = date.getHours();
        int min = date.getMinutes();
        firebaseFirestore.collection("Accidents").document(year).collection(month).document(day).
                collection("accidentAtThisDat3").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        //Log.d("val", documentSnapshot.getId() + " => " + documentSnapshot.getData().get("hours"))
//                        int accidentHr= (int) documentSnapshot.getData().get("hours");
//                        int accidentMin= (int) documentSnapshot.getData().get("minutes");
                       // Log.d("see", "onComplete: "+ hr +" "+min);
                        Pair pair = new Pair(documentSnapshot.getData().get("latitude"),
                                documentSnapshot.getData().get("longitude"));
                        arrayList.add(pair);
                       // Log.d("see", "onComplete: "+accidentHr + " "+ accidentMin);

                    }

                    for(Pair i: arrayList){
                       Log.d("see",i.first.toString()+" "+i.second.toString());
                    }
                }
            }
        });

    }

    private void store(double lng, double lat) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        HashMap<String,Object>map =  new HashMap<>();
        Date date = Calendar.getInstance().getTime();
        String year = String.valueOf(date.getYear());
        String month =  String.valueOf(date.getMonth());
        String day =  String.valueOf(date.getDate());
        String uid =  firebaseAuth.getUid().toString();
        int hr =   date.getHours();
        int min =  date.getMinutes();


        Log.d("acii", year +" "+ month);
        Log.d("acii",day+ " " + hr + " " + min);
        map.put("longitude",lng);
        map.put("latitude",lat);
        map.put("hours",hr);
        map.put("minutes",min);
        map.put("Uid",uid);

        firebaseFirestore.collection("Accidents").document(year).collection(month).document(day).
                collection("accidentAtThisDate").add(map);
    }


}
