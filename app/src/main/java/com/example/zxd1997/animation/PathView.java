package com.example.zxd1997.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PathView extends View {
    Paint paint=new Paint();
    private MainActivity.Path path;

    public MainActivity.Path getPath() {
        return path;
    }

    public void setPath(MainActivity.Path path) {
        this.path = path;
    }

    public PathView(Context context) {
        super(context);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(10);
        canvas.drawLine(path.pre_x, path.pre_y, path.end_x, path.end_y,paint);
    }
}
