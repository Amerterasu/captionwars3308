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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    final String TAG = "MAINACTIVITY";

    private RecyclerView mRecycleView;

    //Firebase variables
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseRecyclerAdapter<Caption, CaptionViewHolder> mAdapter;
    private DatabaseReference ref;
    private DatabaseReference mDatabase;
    private StorageReference imageRef;
    private ImageView captionImage;
    private TextView authorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get intent info
        Intent intent = getIntent();
        String author = intent.getStringExtra("Author");
        String imagePath = intent.getStringExtra("ImagePath");
        int likes = intent.getIntExtra("Likes",0);

        //UI initialization
        Button sendButton = (Button) findViewById(R.id.send_button);
        final EditText commentEditText = (EditText) findViewById(R.id.comment_editText);
        captionImage = (ImageView)findViewById(R.id.capphoto_imageView);


        //Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Caption/");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imagePath);
        if(mFirebaseUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }else{
            //TODO: Add logic for a user signed in
        }

        //set imageview to the selected photo
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(captionImage);
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
                    viewHolder.setUsername(model.getUsername());
                    viewHolder.setCaption(model.getCaption());
                    viewHolder.incrementLikes(model);
                    viewHolder.setCounter(Integer.toString(model.getLikes()));
                }
            };
            mRecycleView.setAdapter(mAdapter);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key = ref.push().getKey();

                    String caption = commentEditText.getText().toString();
                    String username = mFirebaseUser.getEmail();

                    Caption newCap = new Caption(username, 0, caption, key);
                    Map<String, Object> newCapValues = newCap.toMap();

                    Map<String, Object> childUpdates = new HashMap<String, Object>();
                    childUpdates.put("/Caption/"+key, newCapValues);

                    mDatabase.updateChildren(childUpdates);

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
        String username, caption, key;
        int likes;
        Uri imageUrl;
        public Caption(){}

        public Caption(String username, int likes, String caption, String key){
            this.caption = caption;
            this.likes = likes;
            this.username = username;
            this.key = key;

        }

        public int getLikes(){
            return likes;
        }

        public String getUsername(){
            return username;
        }
        public String getCaption(){
            return caption;
        }
        public String getKey(){return key;}

        @Exclude
        public Map<String, Object> toMap(){
            HashMap<String, Object> result = new HashMap<>();
            result.put("username", username);
            result.put("caption", caption);
            result.put("likes", likes);
            result.put("key", key);

            return result;
        }
    }
    public static class CaptionViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public CaptionViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }
        public void setUsername(String name) {
            TextView field = (TextView) mView.findViewById(R.id.username_textview);
            field.setText(name);
        }

        public void setCaption(String text) {
            TextView field = (TextView) mView.findViewById(R.id.caption_textview);
            field.setText(text);
        }

        public void setCounter(String count){
            TextView counter = (TextView) mView.findViewById(R.id.like_counter_textview);
            counter.setText(count);
        }

        public void incrementLikes(Caption data){
            Button likeButton = (Button) mView.findViewById(R.id.like_button);

            final Caption theData = data;
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentLikes = theData.getLikes();
                    int updated_likes = currentLikes + 1;
                    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                    TextView counter = (TextView) mView.findViewById(R.id.like_counter_textview);

                    Caption newData = new Caption(theData.getUsername(), updated_likes, theData.getCaption(), theData.getKey());
                    Map<String, Object> newCapValues = newData.toMap();
                    Map<String, Object> childUpdates = new HashMap<String, Object>();
                    childUpdates.put("/Caption/"+ theData.getKey(), newCapValues);
                    mRef.updateChildren(childUpdates);
                    counter.setText(Integer.toString(updated_likes));
                }
            });
        }

    }

}
