package io.intrepid.russell.swirl;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.image)
    ImageView imageView;

    @BindDrawable(R.drawable.swirl_anim)
    Drawable swirlDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) swirlDrawable;
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
