package takshak.mace.takshak2k18;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Gallery extends AppCompatActivity {
    private static final String TAG = "imageurl";
    FirebaseDatabase database;
    DatabaseReference myRef;
    GridView gridView;
    ImageView expimgview;
    LinearLayout expndlayout;
    RelativeLayout containerview;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        database = FirebaseDatabase.getInstance();
        expimgview = findViewById(R.id.expandedimgview);
        expndlayout = findViewById(R.id.expandedlayout);
        setTitle("Takshak Gallery");
        //containerview = findViewById(R.id.containerlayout);

        /*String[] i = {"https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4d/Cat_November_2010-1a.jpg/200px-Cat_November_2010-1a.jpg",
        "https://lh3.googleusercontent.com/aR34MxRBretppyADbJcfqIZp-LraO1ELhk00lTZw0Q7MF1ebUKZeggeQkjBuZCCmYRSYNzr8=w640-h400-e365",
        "https://cdn.pixabay.com/photo/2018/04/28/22/03/dawn-3358468_1280.jpg",
        "https://cdn.pixabay.com/photo/2017/04/16/01/53/girl-2233820_960_720.jpg",
        "https://cdn.pixabay.com/photo/2017/07/12/22/52/woman-2498668_1280.jpg"};
*/
        gridView = findViewById(R.id.gridview);
        gridView = (GridView) findViewById(R.id.gridview);

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Gallery").setMessage("Getting ready in a moment...").setCancelable(false);
        alertDialog = builder.create();

        expndlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expndlayout.setVisibility(View.GONE);
            }
        });

        myRef = database.getReference("imageurl");
        if (!alertDialog.isShowing()){
            alertDialog.show();
        }
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (alertDialog.isShowing()){
                    alertDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Click to Expand",Toast.LENGTH_SHORT).show();
                }
                ArrayList<String> urls = new ArrayList<>();
                for (DataSnapshot urlSnapshot: dataSnapshot.getChildren()) {
                    String value = urlSnapshot.getValue(String.class);
                    Log.d(TAG, "Value is: " + value);
                    urls.add(value);

                }
                Collections.reverse(urls);
                String[] i = urls.toArray(new String[0]);
                gridView.setAdapter(new ImageAdapter(Gallery.this,i,expimgview,expndlayout));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
