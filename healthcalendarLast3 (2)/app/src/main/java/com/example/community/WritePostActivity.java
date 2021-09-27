package com.example.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.healthcalendar.MainActivity;
import com.example.healthcalendar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class WritePostActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    public Post p;

    public static int postNum = 0; // 글 번호

    private Spinner spinner2;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewListActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writepostfragment);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button cancel = (Button) findViewById(R.id.button);
        Button complete = (Button) findViewById(R.id.button2);
        final EditText title1 = (EditText) findViewById(R.id.title1);
        final EditText mainText1 = (EditText) findViewById(R.id.mainText1);

        //    mDatabase.child("maxPostNum").setValue(0);
        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MaxPostNum value = snapshot.getValue(MaxPostNum.class);
                        postNum = value.getMaxPostNum();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );


        arrayList = new ArrayList<>();
        arrayList.add("운동");
        arrayList.add("식단");
        arrayList.add("함께해요");
        arrayList.add("자유");
        arrayList.add("유머");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(arrayAdapter);


        // 작성 취소 버튼 클릭 시
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewListActivity.class);
                startActivity(intent);
            }
        });

        // 글 작성 버튼 클릭 시
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getTitle = title1.getText().toString();
                String getMainText = mainText1.getText().toString();
                String getCategory = spinner2.getSelectedItem().toString();

                if(getTitle.length()>=30){
                    Toast.makeText(WritePostActivity.this, "제목을 30자 미만으로 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(getMainText.length()<10){
                    Toast.makeText(WritePostActivity.this, "내용을 10자 이상으로 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    writePost(FirebaseAuth.getInstance().getCurrentUser().getUid(), postNum, getTitle, getMainText, getCategory);
                    postNum++;
                    mDatabase.child("maxPostNum").setValue(postNum);

                    Intent intent = new Intent(getApplicationContext(), ViewListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /* 데이터 추가 함수 */
    private void writePost(String userID, int postNum, String title, String mainText, String category) {
        Post pp = new Post(title, postNum, mainText, category, userID, 0, 0);
        String pn = postNum + "";
        mDatabase.child("posts").child(userID).child(pn).setValue(pp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(WritePostActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(WritePostActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}