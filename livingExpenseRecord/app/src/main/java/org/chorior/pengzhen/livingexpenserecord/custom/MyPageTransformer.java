package org.chorior.pengzhen.livingexpenserecord.custom;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by pengzhen on 4/11/16.
 */

public class MyPageTransformer implements ViewPager.PageTransformer {

    public enum TransitionEffect{
        Accordion,
        CubeIn,
        CubeOut,
        Depth,
        FlipHorizontal,
        FlipVertical,
        RotateDown,
        RotateUp,
        Stack,
        Tablet,
        ZoomIn,
        ZoomOut,
    }

    private TransitionEffect effect = TransitionEffect.Accordion;
    public MyPageTransformer(TransitionEffect effect){
        this.effect = effect;
    }

    public void transformPage(View view, float position) {
        switch (effect){
            case Accordion:
                AccordionPageTransformer(view,position);
                break;
            case Depth:
                DepthPageTransformer(view,position);
                break;
            case CubeIn:
                CubeInPageTransformer(view,position);
                break;
            case CubeOut:
                CubeOutPageTransformer(view,position);
                break;
            case FlipHorizontal:
                FlipHorizontalPageTransformer(view,position);
                break;
            case FlipVertical:
                FlipVerticalPageTransformer(view,position);
                break;
            case RotateDown:
                RotateDownPageTransformer(view,position);
                break;
            case RotateUp:
                RotateUpPageTransformer(view,position);
                break;
            case Stack:
                StackPageTransformer(view,position);
                break;
            case Tablet:
                TabletPageTransformer(view,position);
                break;
            case ZoomIn:
                ZoomInPageTransformer(view,position);
                break;
            case ZoomOut:
                ZoomOutPageTransformer(view,position);
                break;
            default:
        }
    }

    private void ZoomOutPageTransformer(View view, float position){
        final float MIN_SCALE = 0.85f;
        final float MIN_ALPHA = 0.5f;

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }

    private void DepthPageTransformer(View view, float position){
        final float MIN_SCALE = 0.75f;

        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }

    private void AccordionPageTransformer(View view, float position){
        if(position < -1 || position > 1){
            view.setAlpha(0);
        }else if(position <= 0) {
            view.setAlpha(1);
            view.setPivotX(view.getWidth());
            view.setScaleX(1f + position);
        }else{
            view.setAlpha(1);
            view.setPivotX(0);
            view.setScaleX(1f - position);
        }
    }

    private void TabletPageTransformer(View view, float position){
        if(position < -1 || position > 1){
            view.setAlpha(0);
        }else{
            final Matrix OFFSET_MATRIX = new Matrix();
            final Camera OFFSET_CAMERA = new Camera();
            float[] OFFSET_TEMP_FLOAT = new float[2];

            float degrees = (position < 0 ? 30f : -30f) * Math.abs(position);
            OFFSET_MATRIX.reset();
            OFFSET_CAMERA.save();
            OFFSET_CAMERA.rotateY(Math.abs(degrees));
            OFFSET_CAMERA.getMatrix(OFFSET_MATRIX);
            OFFSET_CAMERA.restore();

            OFFSET_MATRIX.preTranslate(- view.getWidth() * 0.5f, - view.getHeight() * 0.5f);
            OFFSET_MATRIX.postTranslate(view.getWidth() * 0.5f, view.getHeight() * 0.5f);
            OFFSET_TEMP_FLOAT[0] = view.getWidth();
            OFFSET_TEMP_FLOAT[1] = view.getHeight();
            OFFSET_MATRIX.mapPoints(OFFSET_TEMP_FLOAT);
            float translationX = (view.getWidth() - OFFSET_TEMP_FLOAT[0]) * (degrees > 0.0f ? 1.0f : -1.0f);

            view.setAlpha(1);
            view.setTranslationX(translationX);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(0);
            view.setRotationY(degrees);
        }

    }

    private void CubeInPageTransformer(View view, float position){
        // Rotate the fragment on the left or right edge
        if(position < -1 || position > 1){
            view.setAlpha(0);
        }else if(position <= 0){
            view.setAlpha(1);
            view.setPivotX(view.getWidth());
            //view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(90f * position);
        }else{
            view.setAlpha(1);
            view.setPivotX(0);
            //view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(-90f * position);
        }
    }

    private void CubeOutPageTransformer(View view, float position){
        if(position < -1 || position > 1){
            view.setAlpha(0);
        }else if(position <= 0){
            view.setAlpha(1);
            view.setPivotX(view.getWidth());
            //view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(-90f * position);
        }else{
            view.setAlpha(1);
            view.setPivotX(0);
            //view.setPivotY(view.getHeight() * 0.5f);
            view.setRotationY(90f * position);
        }
    }

    private void FlipVerticalPageTransformer(View view, float position){
        if(position < -1 || position > 1){
            view.setAlpha(0);
        }else if(position <= 0){
            view.setAlpha(1);
            view.setRotationX(-180f * position);
        }else{
            view.setAlpha(1);
            view.setRotationX(180f * position);
        }
    }

    private void FlipHorizontalPageTransformer(View view, float position){
        if(position < -1 || position > 1){
            view.setAlpha(0);
        }else if(position <= 0){
            view.setAlpha(1);
            view.setRotationY(-180f * position);
        }else{
            view.setAlpha(1);
            view.setRotationY(180f * position);
        }
    }

    private void StackPageTransformer(View view, float position) {
        if (position < -1 || position > 1) {
            view.setAlpha(0);
        } else if (position <= 0) {
            view.setAlpha(1);
            view.setTranslationX(0);
        }else{
            view.setAlpha(1);
            view.setScaleX(0.5f * (position + 1));
            view.setScaleY(0.5f * (position + 1));
            //view.setTranslationX(view.getWidth() * position);
        }
    }

    private void ZoomInPageTransformer(View view, float position){
        if (position < -1 || position > 1) {
            view.setAlpha(0);
        } else if(position <= 0){
            view.setAlpha(position + 1f);
            view.setScaleX(position + 1f);
            view.setScaleY(position + 1f);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
        }else{
            view.setAlpha(1f - position);
            view.setScaleX(1f - position);
            view.setScaleY(1f - position);
            view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(view.getHeight() * 0.5f);
        }
    }

    private void RotateUpPageTransformer(View view, float position){
        if (position < -1 || position > 1) {
            view.setAlpha(0);
        } else if(position <= 0){
            float height = view.getHeight();
            view.setAlpha(1);
            //view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(0f);
            view.setTranslationY(-(float)(height*(1 - Math.cos(-15f * position * Math.PI/180f))));
            view.setRotation(360f * position);
        }else{
            view.setAlpha(1);
            float height = view.getHeight();
            //view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(0f);
            view.setTranslationY(-(float)(height*(1 - Math.cos(-15f * position * Math.PI/180f))));
            view.setRotation(360f * (position - 1));
        }
    }

    private void RotateDownPageTransformer(View view, float position){
        if (position < -1 || position > 1) {
            view.setAlpha(0);
        } else if(position <= 0){
            float height = view.getHeight();
            view.setAlpha(1);
            //view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(0f);
            view.setTranslationY(-(float)(height*(1 - Math.cos(-15f * position * Math.PI/180f))));
            view.setRotation(-360f * position);
        }else{
            view.setAlpha(1);
            float height = view.getHeight();
            //view.setPivotX(view.getWidth() * 0.5f);
            view.setPivotY(0f);
            view.setTranslationY(-(float)(height*(1 - Math.cos(-15f * position * Math.PI/180f))));
            view.setRotation(-360f * (position - 1));
        }
    }
}
