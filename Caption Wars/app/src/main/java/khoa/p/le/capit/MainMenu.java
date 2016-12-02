package khoa.p.le.capit;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainMenu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference dataRef;
    private StorageReference storageRef;
    private StorageReference userRef;
    private FirebaseStorage storage;
    private ListView previewList;
    private ListAdapter adapter;
    private DatabaseReference previewRef;

    public static class CaptionPreview{
        String Author,ImagePath;
        int Likes;

        public CaptionPreview(){}

        public CaptionPreview(String author, String path, int likes){
            this.Author = author;
            this.ImagePath = path;
            this.Likes = likes;
        }

        public String getAuthor(){return Author;}
        public String getImagePath(){return ImagePath;}
        public int getLikes(){return Likes;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://project-426870672605395942.appspot.com/");
        userRef = storageRef.child("Users/Khoa/Caption.png");
        previewRef = FirebaseDatabase.getInstance().getReference("Preview/");

        previewList = (ListView) findViewById(R.id.preview_list);

        //user not signed in
        if(mFirebaseUser == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }




        adapter= new FirebaseListAdapter<CaptionPreview>(this, CaptionPreview.class, R.layout.list_caption_item, previewRef) {
            @Override
            protected void populateView(View v, CaptionPreview model, int position) {
                storageRef = storage.getReferenceFromUrl(model.getImagePath());
                ((TextView)v.findViewById(R.id.author_textView)).setText(model.getAuthor());
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .override(400, 400)
                        .into((ImageView)v.findViewById(R.id.caption_preview_image));
            }
        };
        previewList.setAdapter(adapter);
        previewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent captionView = new Intent();
                captionView.setClass(getApplicationContext(), MainActivity.class);
                CaptionPreview passItem = (CaptionPreview)adapter.getItem(pos);

                //Pass info for other Caption Activity to load
                captionView.putExtra("Author", passItem.getAuthor());
                captionView.putExtra("ImagePath", passItem.getImagePath());
                captionView.putExtra("Likes", passItem.getLikes());
                startActivity(captionView);
            }
        });

    }
}
