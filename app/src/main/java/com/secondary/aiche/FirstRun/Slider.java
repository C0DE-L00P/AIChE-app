package com.secondary.aiche.FirstRun;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secondary.aiche.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Slider extends Activity {

    private LinearLayout mDotLayout;
    private Button mStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider_screen);

        ViewPager mSlideViewPager = findViewById(R.id.view_pager_slider);
        mDotLayout = findViewById(R.id.linear_layout_slider);
        mStart = findViewById(R.id.end_slider);

        SliderAdapter sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
    }

    void addDotsIndicator(int position){
        TextView[] mDot = new TextView[3];
        mDotLayout.removeAllViews();

        for(int i = 0; i < mDot.length ; i++){
            mDot[i] = new TextView(this);
            mDot[i].setText(Html.fromHtml("&#8226;"));
            mDot[i].setTextSize(35);
            mDot[i].setTextColor(getResources().getColor(R.color.cardview_dark_background));

            mDotLayout.addView(mDot[i]);
        }
        if(mDot.length > 0){
            mDot[position].setTextColor(getResources().getColor(R.color.cardview_light_background));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            // خلي بالك اعلي position هو n - 1
            if(position == 2) {
                mStart.setTextColor(getResources().getColor(R.color.cardview_light_background));

                mStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }


    //لو عايز تضيف صور للسلايدر

    private int[] slide_images = {
            R.drawable.slider1,
            R.drawable.slider2,
            R.drawable.slider3
    };


    //عشان تضيف نص

    private String[] slide_headings = {
            "Realtime Database","Events Notification","Gathered Materials"
    };

    private String[] slide_text =  {
            "Get the latest events instantly once declared","built-in notification service to notify you","Any data in refining required is in your hands"
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container,int position){
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container,false);

        ImageView slideImageView = view.findViewById(R.id.imageView);
        TextView slideTextView1 = view.findViewById(R.id.slidertext1);
        TextView slideTextView2 = view.findViewById(R.id.slidertext2);

        slideImageView.setImageResource(slide_images[position]);
        slideTextView1.setText(slide_headings[position]);
        slideTextView2.setText(slide_text[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout)object);
    }
}