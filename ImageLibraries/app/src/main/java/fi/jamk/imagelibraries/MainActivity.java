package fi.jamk.imagelibraries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String image_titles[] = {"Spring", "Summer", "Fall", "Winter"};
    private final String image_paths[] = {
            "http://www.cc.puv.fi/~e1500941/images/spring.jpeg",
            "http://www.cc.puv.fi/~e1500941/images/summer.jpeg",
            "http://www.cc.puv.fi/~e1500941/images/autumn.jpeg",
            "http://www.cc.puv.fi/~e1500941/images/winter.jpeg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<CreateList> createLists = prepareData();
        ImageAdapter adapter = new ImageAdapter(getApplicationContext(), createLists);

        recyclerView.setAdapter(adapter);
    }

    private ArrayList<CreateList> prepareData() {
        ArrayList<CreateList> images = new ArrayList<>();

        for (int i = 0; i < image_titles.length; i++) {
            CreateList imageList = new CreateList();
            imageList.setTitle(image_titles[i]);
            imageList.setPath(image_paths[i]);
            images.add(imageList);
        }

        return images;
    }
}
