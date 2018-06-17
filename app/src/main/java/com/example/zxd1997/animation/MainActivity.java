package com.example.zxd1997.animation;

import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.file.attribute.BasicFileAttributeView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    class Path{
        public float pre_x;
        public float end_x;
        public float pre_y;
        public float end_y;
        public int duration;
        public Path(float pre_x, float end_x, float pre_y, float end_y, int duration) {
            this.pre_x = pre_x;
            this.end_x = end_x;
            this.pre_y = pre_y;
            this.end_y = end_y;
            this.duration = duration;
        }
    }
    boolean isTouching=true;
    boolean isRec=false;
    boolean dir=true;
    boolean isRep=false;
    ImageView birdView;
    ImageView dot;
    boolean rec_dir;
    AnimationDrawable bird;
    AnimationDrawable dots;
    PathView path;
    List<Path> paths=new ArrayList<>();
    float w;
    float h;
    float save_x;
    float save_y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        birdView=findViewById(R.id.birdView);
        path=findViewById(R.id.path);
        path=findViewById(R.id.path);
        path.setPath(new Path(0,0,0,0,0));
        bird= (AnimationDrawable) birdView.getDrawable();
        dot =findViewById(R.id.dot);
        bird.start();
        dots= (AnimationDrawable) dot.getDrawable();
        dots.start();
        DisplayMetrics displayMetrics=getResources().getDisplayMetrics();
        w=displayMetrics.widthPixels;
        h=displayMetrics.heightPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                if (isTouching&&!isRep){
                    float x=event.getX();
                    float y=event.getY()-200;
                    float bw=birdView.getWidth();
                    float bh= birdView.getHeight();
                    float bx=birdView.getX();
                    float by=birdView.getY();
                    final float ex=x+bw>w?w-bw:x;
                    final float ey=y+bh>h?h-bh:y;
                    float xx=ex-bx;
                    float yy=ey-by;
                    TranslateAnimation translateAnimation=new TranslateAnimation(0,xx,0,yy);
                    if (xx<0&&dir){
                        dir=false;
                        ObjectAnimator.ofFloat(birdView,"rotationY",0.0f,180.0f).setDuration(500).start();
                        birdView.setRotationY(180.0f);
                        translateAnimation.setStartOffset(500);
                    }
                    if (xx>0&&!dir){
                        dir=true;
                        ObjectAnimator.ofFloat(birdView,"rotationY",180.0f,0.0f).setDuration(500).start();
                        birdView.setRotationY(180.0f);
                        translateAnimation.setStartOffset(500);
                    }
                    int duration=(int)(5000*(Math.sqrt(xx*xx+yy*yy)/Math.sqrt(w*w+h*h)));
                    if (isRec){
                        paths.add(new Path(bx,ex,by,ey,duration));
                    }
                    translateAnimation.setDuration(duration);
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isTouching=false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            birdView.setX(ex);
                            birdView.setY(ey);
                            birdView.clearAnimation();
                            isTouching=true;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    birdView.startAnimation(translateAnimation);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.begin_rec:{
                if (!isRec&&!isRep){
                    Toast.makeText(this,"开始录像",Toast.LENGTH_LONG).show();
                    isRec=true;
                    paths.clear();
                    rec_dir=dir;
                    dot.setVisibility(View.VISIBLE);
                }
                break;
            }
            case R.id.stop_rec:{
                if (isRec){
                    Toast.makeText(this,"停止录像",Toast.LENGTH_LONG).show();
                    Log.d("path", "onOptionsItemSelected: "+paths.size());
                    isRec=false;
                    dot.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.begin_rep:{
                if (!isRec&&!isRep){
                    Toast.makeText(this,"开始回放",Toast.LENGTH_LONG).show();
                    save_x=birdView.getX();
                    save_y= birdView.getY();
                    isTouching=false;
                    isRep=true;
                    replay(paths.iterator());
                }
                break;
            }
            case R.id.stop_rep:{
                if (isRep){
                    Toast.makeText(this,"停止回放",Toast.LENGTH_LONG).show();
                    path.setPath(new Path(0,0,0,0,0));
                    path.invalidate();
                    isRep=false;
                    birdView.setX(save_x);
                    birdView.setY(save_y);
                    birdView.clearAnimation();
                    isTouching=true;
                }
                break;
            }
        };
        return true;
    }

    private void replay(final Iterator<Path> iterator) {
        final Path p=iterator.next();
        float xx=p.end_x-p.pre_x;
        float yy=p.end_y-p.pre_y;
        birdView.setX(p.pre_x);
        birdView.setY(p.pre_y);
        path.setPath(p);
        path.invalidate();
        TranslateAnimation translateAnimation=new TranslateAnimation(0,xx,0,yy);
        if (xx<0&&rec_dir){
            rec_dir=false;
            ObjectAnimator.ofFloat(birdView,"rotationY",0.0f,180.0f).setDuration(500).start();
            birdView.setRotationY(180.0f);
            translateAnimation.setStartOffset(500);
        }
        if (xx>0&&!rec_dir){
            rec_dir=true;
            ObjectAnimator.ofFloat(birdView,"rotationY",180.0f,0.0f).setDuration(500).start();
            birdView.setRotationY(180.0f);
            translateAnimation.setStartOffset(500);
        }
        translateAnimation.setDuration(p.duration);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isTouching=false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                birdView.setX(p.end_x);
                birdView.setY(p.end_y);
                if (iterator.hasNext()&&isRep){
                    replay(iterator);
                }else {
                    birdView.setX(save_x);
                    birdView.setY(save_y);
                    birdView.clearAnimation();
                    path.setPath(new Path(0,0,0,0,0));
                    path.invalidate();
                    isTouching=true;
                    isRep=false;
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        birdView.startAnimation(translateAnimation);
    }
}
