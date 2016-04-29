package io.intrepid.russell.swirl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = (ImageView) findViewById(R.id.image);

        assert image != null;
        image.setImageDrawable(new SwirlDrawable(getResources().getDisplayMetrics().density));
    }
}
