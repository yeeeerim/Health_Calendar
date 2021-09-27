package com.example.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewPostActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    public static int commentNum = 0; // 댓글 번호
    public ArrayList<Comment> data = new ArrayList<>();
    CommentAdapter adapter;
    boolean check = true;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewListActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpostfragment);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button gotoListBT = (Button) findViewById(R.id.button8);
        Button writeCommentBT = (Button) findViewById(R.id.button9);
        ImageButton goodBT = (ImageButton)findViewById(R.id.button6);
        ImageButton hateBT = (ImageButton)findViewById(R.id.button7);
        Button deletePostBT = (Button)findViewById(R.id.button5);
        Button editPostBT = (Button)findViewById(R.id.button4);

        Intent intent = getIntent();

        final TextView title = (TextView) findViewById(R.id.textView4);
        final TextView userID = (TextView) findViewById(R.id.textView5);
        final TextView mainText = (TextView) findViewById(R.id.textView3);
        final TextView goodCnt = (TextView) findViewById(R.id.textView);
        final TextView hateCnt = (TextView) findViewById(R.id.textView2);
        final EditText comment = (EditText) findViewById(R.id.inputComment);


        mainText.setMovementMethod((new ScrollingMovementMethod()));
        final ListView commentList = (ListView) findViewById(R.id.commentlist);


        final int postNum0 = intent.getIntExtra("postNum", 1);
        final String userID0 = intent.getStringExtra("userID");


        final Context c = this;


        mDatabase.child("posts").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postData : snapshot.getChildren()){
                            for(DataSnapshot postData2 : postData.getChildren()){
                                Post p = postData2.getValue(Post.class);
                                if(p.getPostNum()==postNum0){
                                    int goodCnt1 = p.getGoodCnt();
                                    int hateCnt1 = p.getHateCnt();

                                    goodCnt.setText(goodCnt1+"");
                                    hateCnt.setText(hateCnt1+"");
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        mDatabase.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        MaxCommentNum value = snapshot.getValue(MaxCommentNum.class);
                        commentNum = value.getMaxCommentNum();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );


        mDatabase.child("comments").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot commentData : snapshot.getChildren()){
                            Comment c = commentData.getValue(Comment.class);
                            if(c.getPostNum()==postNum0){
                                int commentNum = c.getCommentNum();
                                String commentText = c.getCommentText();
                                data.add(new Comment(commentText, commentNum));
                            }
                        }
                        adapter = new CommentAdapter(c, R.layout.comment_item, data);
                        commentList.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        mDatabase.child("Users").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userID.setText(snapshot.child(userID0).child("nickname").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
        mDatabase.child("posts").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postData : snapshot.getChildren()) {
                            for (DataSnapshot postData2 : postData.getChildren()) {
                                Post p = postData2.getValue(Post.class);
                                if (p.getPostNum() == postNum0) {
                                    title.setText(p.getTitle());
                                    mainText.setText(p.getMainText());
                                    goodCnt.setText(p.getGoodCnt()+"");
                                    hateCnt.setText(p.getHateCnt()+"");
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

        goodBT.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (check) {
                    check = false;
                    mDatabase.child("posts").addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot postData : snapshot.getChildren()) {
                                        for (DataSnapshot postData2 : postData.getChildren()) {
                                            Post p = postData2.getValue(Post.class);
                                            if (p.getPostNum() == postNum0) {
                                                int goodCnt1 = p.getGoodCnt();

                                                ++goodCnt1;

                                                mDatabase.child("posts").child(p.getUserID()).child(p.getPostNum() + "").child("goodCnt").setValue(goodCnt1);

                                                goodCnt.setText(goodCnt1 + "");
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }
                    );
                }
                else{
                    Toast.makeText(c, "이미 평가하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 노력해요 버튼 클릭 시
        hateBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    check = false;
                    mDatabase.child("posts").addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot postData : snapshot.getChildren()) {
                                        for (DataSnapshot postData2 : postData.getChildren()) {
                                            Post p = postData2.getValue(Post.class);
                                            if (p.getPostNum() == postNum0) {
                                                int hateCnt1 = p.getHateCnt();

                                                ++hateCnt1;

                                                mDatabase.child("posts").child(p.getUserID()).child(p.getPostNum() + "").child("hateCnt").setValue(hateCnt1);

                                                hateCnt.setText(hateCnt1 + "");
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }
                    );
                }
                else{
                    Toast.makeText(c, "이미 평가하셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        // 잘했어요 버튼 클릭 시
//        goodBT.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                mDatabase.child("posts").addListenerForSingleValueEvent(
//                        new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for(DataSnapshot postData : snapshot.getChildren()){
//                                    for(DataSnapshot postData2 : postData.getChildren()){
//                                        Post p = postData2.getValue(Post.class);
//                                        if(p.getPostNum()==postNum0){
//                                            int goodCnt1 = p.getGoodCnt();
//
//                                            ++goodCnt1;
//
//                                            mDatabase.child("posts").child(p.getUserID()).child(p.getPostNum()+"").child("goodCnt").setValue(goodCnt1);
//
//                                            goodCnt.setText(goodCnt1+"");
////                                    Intent intent = getIntent();
////                                    startActivity(intent);
//                                        }
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        }
//                );
//            }
//        });
//
//
//        // 노력해요 버튼 클릭 시
//        hateBT.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDatabase.child("posts").addListenerForSingleValueEvent(
//                        new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for(DataSnapshot postData : snapshot.getChildren()){
//                                    for(DataSnapshot postData2 : postData.getChildren()){
//                                        Post p = postData2.getValue(Post.class);
//                                        if(p.getPostNum()==postNum0){
//                                            int hateCnt1 = p.getHateCnt();
//
//                                            ++hateCnt1;
//
//                                            mDatabase.child("posts").child(p.getUserID()).child(p.getPostNum()+"").child("hateCnt").setValue(hateCnt1);
//
//                                            hateCnt.setText(hateCnt1+"");
////                                            Intent intent = getIntent();
////                                            startActivity(intent);
//                                        }
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        }
//                );
//            }
//        });

        // 목록보기 버튼 클릭 시
        gotoListBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewListActivity.class);
                startActivity(intent);
            }
        });

        // 글 삭제 버튼 클릭 시
        deletePostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userID0.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    mDatabase.child("posts").child(userID0).child(postNum0+"").removeValue();
                    mDatabase.child("comments").addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot commentData : snapshot.getChildren()){
                                        Comment c = commentData.getValue(Comment.class);
                                        if(c.getPostNum()==postNum0) {
                                            mDatabase.child("comments").child(c.getCommentNum()+"").removeValue();
                                        }

                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }
                    );

                    Toast.makeText(c, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewListActivity.class);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(ViewPostActivity.this, "본인만 삭제할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //글 수정 버튼 클릭 시
        editPostBT.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if(userID0.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    Intent intent = new Intent(getApplicationContext(), EditPostActivity.class);

                    intent.putExtra("postNum", postNum0);
                    intent.putExtra("userID", userID0);

                    startActivity(intent);
                }
                else{
                    Toast.makeText(ViewPostActivity.this, "본인만 수정할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 댓글 작성 버튼 클릭 시
        writeCommentBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment0 = comment.getText().toString();
                comment.setText("");

//                HashMap result = new HashMap<>();
//                result.put("comment", comment0);

                writeComment(FirebaseAuth.getInstance().getCurrentUser().getUid(),commentNum, postNum0, comment0);
                commentNum++;
                mDatabase.child("maxCommentNum").setValue(commentNum);

                mDatabase.child("comments").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot commentData : snapshot.getChildren()){
                                    Comment c = commentData.getValue(Comment.class);
                                    if(c.getPostNum()==postNum0){
                                        int commentNum = c.getCommentNum();
                                        String commentText = c.getCommentText();
                                        data.add(new Comment(commentText, commentNum));
                                        //  adapter.notifyDataSetChanged();
                                    }
                                }
                                //     adapter = new CommentAdapter(c, R.layout.comment_item, data);
                                //    commentList.setAdapter(adapter);
                                data.clear();
                                mDatabase.child("comments").addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot commentData : snapshot.getChildren()){
                                                    Comment c = commentData.getValue(Comment.class);
                                                    if(c.getPostNum()==postNum0){
                                                        int commentNum = c.getCommentNum();
                                                        String commentText = c.getCommentText();
                                                        data.add(new Comment(commentText, commentNum));
                                                    }
                                                }
                                                adapter = new CommentAdapter(c, R.layout.comment_item, data);
                                                commentList.setAdapter(adapter);
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        }
                                );
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
            }
        });
    }

    private void writeComment (String userID,int commentNum, int postNum, String comment){
        Comment pp = new Comment(userID,comment, postNum,commentNum);
        String cn = commentNum + "";
        mDatabase.child("comments").child(cn).setValue(pp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        Toast.makeText(ViewPostActivity.this, "댓글 작성 완료", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(ViewPostActivity.this, "댓글 작성 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
