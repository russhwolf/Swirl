package io.intrepid.russell.swirl;

import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final AnimatedVectorDrawableCompat animatedVectorDrawable = AnimatedVectorDrawableCompat.create(this, R.drawable.swirl_anim);
        imageView.setImageDrawable(animatedVectorDrawable);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animatedVectorDrawable.isRunning()) {
                    animatedVectorDrawable.stop();
                } else {
                    animatedVectorDrawable.start();
                }
            }
        });
    }
}
