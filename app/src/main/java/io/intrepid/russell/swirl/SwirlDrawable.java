package io.intrepid.russell.swirl;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.view.animation.LinearInterpolator;

public class SwirlDrawable extends Drawable {

    // GEOMETRIC CONSTANTS
    private static final float VIEWPORT = 400;
    private static final float STROKE = 11.0067f;

    private static final float[] POINTS = {
            67.3f, 243.1f,
            24.6f, -24.6f,
            34.8f, 0,
            77, 0,
            117.4f, 0,
            127.5f, -111.9f,
            58.7f, -111.9f,
            -67.9f, 0,
            -58.7f, 111.9f,
            58.7f, 111.9f,
            37.8f, 0,
            56.5f, -20.5f,
            77, 0
    };

    // Converting POINTS from relative coordinates to exact coordinates
    static {
        for (int i = 20; i < POINTS.length; i += 2) {
            POINTS[i] += POINTS[18];
            POINTS[i + 1] += POINTS[19];
        }
        for (int i = 14; i < POINTS.length; i += 2) {
            POINTS[i] += POINTS[12];
            POINTS[i + 1] += POINTS[13];
        }
        for (int i = 8; i < POINTS.length; i += 2) {
            POINTS[i] += POINTS[6];
            POINTS[i + 1] += POINTS[7];
        }
        for (int i = 2; i < POINTS.length; i += 2) {
            POINTS[i] += POINTS[0];
            POINTS[i + 1] += POINTS[1];
        }
    }

    private static final float CIRCLE_X = 201.7f;
    private static final float CIRCLE_Y = 189.5f;
    private static final float CIRCLE_RADIUS = 108.7f;
    private static final float CIRCLE_START_ANGLE = -90;
    private static final RectF CIRCLE_FRAME = new RectF();

    private static final float[] TMP = new float[6];

    // Size in dp
    private static final float SIZE_DP = 400;
    // size in pixels.
    private final int sizePx;
    // Scale factor between viewport coordinates and pixels.
    private final float scale;

    private final Paint paint = new Paint();
    private final Path path = new Path();

    // For animation
    private static final int ANIMATION_DURATION = 2000;
    private static final int MAX_LEVEL = 10000;
    private static final float[] BREAKS = {
            0 / 12f, 1 / 12f, 2 / 12f, 3 / 12f, 4 / 12f, 6 / 12f, 7 / 12f, 8 / 12f, 9 / 12f, 10 / 12f
    };
    private final ValueAnimator animator = ValueAnimator.ofInt(0, MAX_LEVEL);

    public SwirlDrawable(float density, @ColorInt int color) {
        sizePx = Math.round(SIZE_DP * density);
        scale = (SIZE_DP * density) / VIEWPORT;

        CIRCLE_FRAME.left = px(CIRCLE_X - CIRCLE_RADIUS);
        CIRCLE_FRAME.top = px(CIRCLE_Y - CIRCLE_RADIUS);
        CIRCLE_FRAME.right = px(CIRCLE_X + CIRCLE_RADIUS);
        CIRCLE_FRAME.bottom = px(CIRCLE_Y + CIRCLE_RADIUS);

        initializePaint(color);
        updatePath(0f);
        initializeAnimator();
    }

    private void initializePaint(@ColorInt int color) {
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(px(STROKE));
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initializeAnimator() {
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setLevel((int) animation.getAnimatedValue());
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new LinearInterpolator());
    }

    public void toggleAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // On Kitkat, we can pause/resume.
            if (animator.isPaused()) {
                animator.resume();
            } else if (animator.isRunning()) {
                animator.pause();
            } else {
                animator.start();
            }
        } else {
            // Otherwise, we can only start/end
            if (animator.isRunning()) {
                animator.end();
            } else {
                animator.start();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        updatePath(1f * getLevel() / MAX_LEVEL);
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

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    private void updatePath(@FloatRange(from = 0, to = 1) float t) {
        path.reset();

        if (t < 0.5f) {
            float tc = percentDistance(BREAKS[0], BREAKS[4], t);
            path.addArc(CIRCLE_FRAME, CIRCLE_START_ANGLE, -360 + tc * 360);

            path.moveTo(px(POINTS[24]), px(POINTS[25]));
            TMP[4] = POINTS[24];
            TMP[5] = POINTS[25];
            drawSegment(3, 3, false, t);
            drawSegment(2, 2, false, t);
            drawSegment(1, 1, false, t);
            drawSegment(0, 0, false, t);
        } else {
            float tc = percentDistance(BREAKS[5], BREAKS[9], t);
            path.addArc(CIRCLE_FRAME, CIRCLE_START_ANGLE, tc * 360);

            path.moveTo(px(POINTS[0]), px(POINTS[1]));
            TMP[4] = POINTS[0];
            TMP[5] = POINTS[1];
            drawSegment(0, 5, true, t);
            drawSegment(1, 6, true, t);
            drawSegment(2, 7, true, t);
            drawSegment(3, 8, true, t);
        }
    }

    private void drawSegment(int lineSection, int timeSection, boolean forward, @FloatRange(from = 0, to = 1) float t) {
        float p0x = TMP[4];
        float p0y = TMP[5];
        float p1x = POINTS[(forward ? 2 : 0) + 6 * lineSection + (forward ? 0 : 4)];
        float p1y = POINTS[(forward ? 2 : 0) + 6 * lineSection + (forward ? 1 : 5)];
        float p2x = POINTS[(forward ? 2 : 0) + 6 * lineSection + (forward ? 2 : 2)];
        float p2y = POINTS[(forward ? 2 : 0) + 6 * lineSection + (forward ? 3 : 3)];
        float p3x = POINTS[(forward ? 2 : 0) + 6 * lineSection + (forward ? 4 : 0)];
        float p3y = POINTS[(forward ? 2 : 0) + 6 * lineSection + (forward ? 5 : 1)];

        float fraction = percentDistance(BREAKS[timeSection], BREAKS[timeSection + 1], t);
        if (!forward) {
            fraction = 1 - fraction;
        }

        parametrizeBezier(p0x, p0y, p1x, p1y, p2x, p2y, p3x, p3y, fraction, TMP);
        path.cubicTo(px(TMP[0]), px(TMP[1]), px(TMP[2]), px(TMP[3]), px(TMP[4]), px(TMP[5]));
    }

    /**
     * Converts a dimension {@code dim} in viewport coordinates to pixels.
     */
    private float px(float dim) {
        return dim * scale;
    }

    /**
     * If {@code t} is between {@code min} and {@code max}, this returns how far {@code t} is between them, as a fraction
     * of {@code (max - min)}. Otherwise it returns {@code 0} for {@code t < min} or {@code 1} for {@code t > max}.
     */
    private float percentDistance(float min, float max, @FloatRange(from = 0, to = 1) float t) {
        float out = (t - min) / (max - min);
        return (t < min ? 0 : t > max ? 1 : out);
    }

    /**
     * Interpolates between {@code a} and {@code b}, returning a value which is a fraction {@code t} between them.
     */
    private static float interp(float a, float b, @FloatRange(from = 0, to = 1) float t) {
        return (1 - t) * a + t * b;
    }

    /**
     * Interpolates along a bezier curve specified by starting point {@code (p0x, p0y)}, through control points specified
     * by {@code (p1x, p1y)} and {@code (p2x, p2y)}, to final point {@code (p3x, p3y)}. Returns, in {@code out}, points
     * specifying a bezier curve which traces a fraction {@code t} of the original curve. The output curve still starts
     * from {@code (p0x, p0y)}, but uses control points {@code (out[0], out[1])} and {@code (out[2], out[3])}, and final
     * point {@code (out[4], out[5])}
     */
    private static void parametrizeBezier(
            float p0x, float p0y, float p1x, float p1y, float p2x, float p2y, float p3x, float p3y,
            @FloatRange(from = 0, to = 1) float t, float[] out) {
        float q0x = interp(p0x, p1x, t);
        float q0y = interp(p0y, p1y, t);
        float q1x = interp(p1x, p2x, t);
        float q1y = interp(p1y, p2y, t);
        float q2x = interp(p2x, p3x, t);
        float q2y = interp(p2y, p3y, t);

        float r0x = interp(q0x, q1x, t);
        float r0y = interp(q0y, q1y, t);
        float r1x = interp(q1x, q2x, t);
        float r1y = interp(q1y, q2y, t);

        float s0x = interp(r0x, r1x, t);
        float s0y = interp(r0y, r1y, t);

        out[0] = q0x;
        out[1] = q0y;
        out[2] = r0x;
        out[3] = r0y;
        out[4] = s0x;
        out[5] = s0y;
    }
}
