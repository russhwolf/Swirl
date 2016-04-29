package io.intrepid.russell.swirl;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

public class SwirlDrawable extends Drawable {

    private static final int VIEWPORT = 400;
    private static final int SIZE_DP = 288;
    private static final int STROKE_DP = 5;

    private final float density;
    private final int sizePx;
    private final float scale;

    private final Paint paint = new Paint();
    private final Path path = new Path();

    public SwirlDrawable(float density)
    {
        this(density, Color.BLACK);
    }

    private float px(double dim) {
        return px((float)dim);
    }

    private float px(float dim) {
        return dim * scale;
    }

    public SwirlDrawable(float density, @ColorInt int color) {
        this.density = density;
        sizePx = Math.round(SIZE_DP * density);
        scale = (SIZE_DP * density) / VIEWPORT;

        initializePaint(color);
        initializePath();
    }

    private void initializePaint(@ColorInt int color) {
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_DP * density);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initializePath() {
        path.moveTo(px(67.3),px(243.1));
        path.rCubicTo(px(24.6),px(-24.6),px(34.8),px(0),px(77),px(0));
        path.rCubicTo(px(117.4),px(0),px(127.5),px(-111.9),px(58.7),px(-111.9));
        path.rCubicTo(px(-67.9),px(0),px(-58.7),px(111.9),px(58.7),px(111.9));
        path.rCubicTo(px(37.8),px(0),px(56.5),px(-20.5),px(77),px(0));

        path.addCircle(px(201.7), px(189.5), px(108.7), Path.Direction.CCW);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public int getAlpha() {
        return paint.getAlpha();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public ColorFilter getColorFilter() {
        return paint.getColorFilter();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return sizePx;
    }

    @Override
    public int getIntrinsicHeight() {
        return sizePx;
    }
}
