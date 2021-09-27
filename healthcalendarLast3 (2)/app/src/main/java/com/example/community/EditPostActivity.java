package com.example.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.healthcalendar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class EditPostActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    public Post p;

    private Spinner spinner2;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    public int a=0, b=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writepostfragment);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();

        final int postNum0 = intent.getIntExtra("postNum", 1);
        String userID0 = intent.getStringExtra("userID");


        Button cancel = (Button) findViewById(R.id.button);
        Button complete = (Button) findViewById(R.id.button2);
        final EditText title1 = (EditText) findViewById(R.id.title1);
        final EditText mainText1 = (EditText) findViewById(R.id.mainText1);



        arrayList = new ArrayList<>();
        arrayList.add("운동");
        arrayList.add("식단");
        arrayList.add("함께해요");
        arrayList.add("자유");
        arrayList.add("유머");

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(arrayAdapter);
        mDatabase.child("posts").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postData : snapshot.getChildren()) {
                            for (DataSnapshot postData2 : postData.getChildren()) {
                                Post p = postData2.getValue(Post.class);
                                if (p.getPostNum() == postNum0) {
                                    a=p.getGoodCnt();
                                    b=p.getHateCnt();
                                    title1.setText(p.getTitle());
                                    mainText1.setText(p.getMainText());
                                    String category = p.getCategory();
                                    for(int i=0; i<arrayList.size(); i++){
                                        if(arrayList.get(i).equals(category)){
                                            spinner2.setSelection(i);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );


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
                    Toast.makeText(EditPostActivity.this, "제목을 30자 미만으로 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(getMainText.length()<10){
                    Toast.makeText(EditPostActivity.this, "내용을 10자 이상으로 작성해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    writePost(FirebaseAuth.getInstance().getCurrentUser().getUid(), postNum0, getTitle, getMainText, getCategory);

                    Intent intent = new Intent(getApplicationContext(), ViewListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /* 데이터 추가 함수 */
    private void writePost(String userID, int postNum, String title, String mainText, String category) {
        Post pp = new Post(title, postNum, mainText, category, userID, a, b);
        String pn = postNum + "";
        mDatabase.child("posts").child(userID).child(pn).setValue(pp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(EditPostActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(EditPostActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}