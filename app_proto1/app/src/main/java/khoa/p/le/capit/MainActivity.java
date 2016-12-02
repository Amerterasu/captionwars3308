package khoa.p.le.capit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    final String TAG = "MAINACTIVITY";

    private RecyclerView mRecycleView;

    //Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseRecyclerAdapter<Caption, CaptionViewHolder> mAdapter;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //send button
        Button sendButton = (Button) findViewById(R.id.send_button);
        final EditText commentEditText = (EditText) findViewById(R.id.comment_editText);
        //Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Caption/");
        if(mFirebaseUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }else{
            //TODO: Add logic for a user signed in
        }


        mRecycleView = (RecyclerView) findViewById(R.id.comments_recycleview);
        mRecycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);

        if (mAuth.getCurrentUser() != null) {
            //Recycler Adapter populates the recycler view
            mAdapter = new FirebaseRecyclerAdapter<Caption, CaptionViewHolder>(
                    Caption.class
                    ,R.layout.recycler_list_item
                    ,CaptionViewHolder.class,
                    ref
            ) {
                @Override
                protected void populateViewHolder(CaptionViewHolder viewHolder, Caption model, int position) {
                    Log.v(TAG, "Populating View");
                    viewHolder.setName(model.getName());
                    viewHolder.setText(model.getUid());
                }
            };
            mRecycleView.setAdapter(mAdapter);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference newChild = ref.push();
                    newChild.child("name").setValue(mFirebaseUser.getEmail());
                    newChild.child("uid").setValue(commentEditText.getText().toString());
                    commentEditText.setText("");
                }
            });
        }else{
            Log.e(TAG, "NOT SIGNED IN CAN'T CREATE ADAPTER");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdapter != null)
            mAdapter.cleanup();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_menu, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sign_out_menu:
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class Caption{
        String name, uid, captionName;
        Uri imageUrl;
        public Caption(){}

        public Caption(String name, String uid){
            this.name = name;
            this.uid = uid;
        }

        public String getName(){
            return name;
        }
        public String getUid(){
            return uid;
        }
    }
    public static class CaptionViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public CaptionViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setName(String name) {
            TextView field = (TextView) mView.findViewById(R.id.name_textView);
            field.setText(name);
        }

        public void setText(String text) {
            TextView field = (TextView) mView.findViewById(R.id.uid_textview);
            field.setText(text);
        }

    }

}
