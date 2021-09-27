package com.example.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthcalendar.MainActivity;
import com.example.healthcalendar.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class ViewListActivity extends AppCompatActivity {
    private Spinner spinner3;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    private DatabaseReference mDatabase;
    public ArrayList<Post> data = new ArrayList<>();
    String category;
    PostAdapter3 adapter;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewlistfragment);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final ListView listView = (ListView) findViewById(R.id.listView);

        final Context c = this;




        /* 아이템 클릭시 작동 */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewPostActivity.class);

                intent.putExtra("postNum", data.get(position).getPostNum());
                intent.putExtra("userID", data.get(position).getUserID());


                startActivity(intent);
            }
        });

        Button writePostBT = (Button)findViewById(R.id.button11);
        ImageButton refresh = (ImageButton)findViewById(R.id.button10);


        writePostBT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(getApplicationContext(), WritePostActivity.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewListActivity.class);
                startActivity(intent);

                finish();
            }
        });

        arrayList = new ArrayList<>();
        arrayList.add("전체");
        arrayList.add("운동");
        arrayList.add("식단");
        arrayList.add("함께해요");
        arrayList.add("자유");
        arrayList.add("유머");


        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinner3 = (Spinner)findViewById(R.id.spinner3);
        spinner3.setAdapter(arrayAdapter);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                data.clear();
                category = arrayList.get(position);
                if(category.equals("전체")){
                    mDatabase.child("posts").addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot postData : snapshot.getChildren()){
                                        for(DataSnapshot postData2 : postData.getChildren()){
                                            Post p = postData2.getValue(Post.class);
                                            int postNum = p.getPostNum();
                                            String title = p.getTitle();
                                            String category2 = p.getCategory();
                                            String userID = p.getUserID();
                                            String mainText = p.getMainText();
                                            int goodCnt = p.getGoodCnt();
                                            int hateCnt = p.getHateCnt();
                                            data.add(new Post(title, postNum, mainText, category2, userID, goodCnt, hateCnt));
                                        }
                                    }
                                    adapter = new PostAdapter3(c, R.layout.post_item, data);
                                    listView.setAdapter(adapter);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }
                    );
                }
                mDatabase.child("posts").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot postData : snapshot.getChildren()){
                                    for(DataSnapshot postData2 : postData.getChildren()){
                                        Post p = postData2.getValue(Post.class);
                                        if(p.getCategory().equals(category)){
                                            int postNum = p.getPostNum();
                                            String title = p.getTitle();
                                            String category2 = p.getCategory();
                                            String userID = p.getUserID();
                                            data.add(new Post(title, category2, postNum, userID));
                                        }
                                    }
                                }
                                PostAdapter3 adapter = new PostAdapter3(c, R.layout.post_item, data);
                                listView.setAdapter(adapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}