package com.edanyma.utils;

import android.content.Context;
import android.graphics.Matrix;
import android.hardware.display.DisplayManager;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;

import java.util.Objects;

public class AutoFitPreviewBuilder implements DisplayManager.DisplayListener {

    private Preview mPreview;
    private TextureView mViewFinder;

    private Integer mBufferRotation;
    private Integer mViewFinderRotation;
    private Size mBufferDimens;
    private Size mViewFinderDimens;
    private Integer mViewFinderDisplay;
    private DisplayManager mDisplayManager;


    public AutoFitPreviewBuilder( PreviewConfig previewConfig, TextureView textureView ) {
        this.mPreview = new Preview( previewConfig );
        this.mViewFinder = textureView;
        this.mBufferDimens = new Size(0, 0);
        this.mViewFinderDimens = new Size( 0,0 );
        init();
    }

    public Preview build(){
        return mPreview;
    }

    private void init(){
        this.mPreview.setOnPreviewOutputUpdateListener( ( Preview.PreviewOutput output ) -> {
            previewOutputUpdateListener( output );
        } );
        this.mViewFinder.addOnLayoutChangeListener( ( View view, int left, int top, int right, int bottom,
                                                      int i4, int i5, int i6, int i7 ) -> {
            TextureView viewFinder = ( TextureView ) view;
            Size newViewFinderDimens = new Size(right - left, bottom - top);
            Integer rotation = getDisplaySurfaceRotation( viewFinder.getDisplay() );
            updateTransform(viewFinder, rotation, mBufferDimens, newViewFinderDimens);
        } );
        this.mDisplayManager = ( DisplayManager ) this.mViewFinder.getContext().getSystemService( Context.DISPLAY_SERVICE );
        this.mDisplayManager.registerDisplayListener( this, null );
        final AutoFitPreviewBuilder me = this;
        this.mViewFinder.addOnAttachStateChangeListener( new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow( View view ) {
                mDisplayManager.registerDisplayListener( me, null );
            }
            @Override
            public void onViewDetachedFromWindow( View view ) {
                mDisplayManager.unregisterDisplayListener( me );
            }
        } );
        this.mViewFinderRotation = getDisplaySurfaceRotation( mViewFinder.getDisplay() ) == null ? 0
                : getDisplaySurfaceRotation( mViewFinder.getDisplay() );
        this.mViewFinderDisplay = mViewFinder.getDisplay().getDisplayId();
    }

    private void updateTransform( TextureView textureView, Integer rotation, Size newBufferDimens, Size newViewFinderDimens ){
        if ( textureView == null ){
            return;
        }
        if (rotation == mViewFinderRotation &&
                Objects.equals(newBufferDimens, mBufferDimens) &&
                Objects.equals(newViewFinderDimens, mViewFinderDimens)) {
            return;
        }
           if (rotation == null) {
                return;
        } else {
            mViewFinderRotation = rotation;
        }

        if (newBufferDimens.getWidth() == 0 || newBufferDimens.getHeight() == 0) {
            return;
        } else {
            mBufferDimens = newBufferDimens;
        }

        if (newViewFinderDimens.getWidth() == 0 || newViewFinderDimens.getHeight() == 0) {
            return;
        } else {
            mViewFinderDimens = newViewFinderDimens;
        }
        Matrix matrix = new Matrix();

        float centerX = (float) mViewFinderDimens.getWidth() / 2f;
        float centerY = (float) mViewFinderDimens.getHeight() / 2f;

        matrix.postRotate( (float)-mViewFinderRotation, centerX, centerY );

        float bufferRatio = mBufferDimens.getHeight() / (float) mBufferDimens.getWidth();

        float scaledWidth;
        float scaledHeight;

        if ( mViewFinderDimens.getWidth() > mViewFinderDimens.getHeight() ) {
            scaledHeight = mViewFinderDimens.getWidth();
            scaledWidth = Math.round( mViewFinderDimens.getWidth() * bufferRatio);
        } else {
            scaledHeight = mViewFinderDimens.getHeight();
            scaledWidth = Math.round( mViewFinderDimens.getHeight() * bufferRatio );
        }


        float xScale =  scaledWidth / (float) mViewFinderDimens.getWidth();
        float yScale = scaledHeight / (float) mViewFinderDimens.getHeight();

        matrix.preScale(xScale, yScale, centerX, centerY);

        textureView.setTransform( matrix );
    }

    private void previewOutputUpdateListener( Preview.PreviewOutput output ){
        ViewGroup parent = ( ViewGroup ) mViewFinder.getParent();
        parent.removeView( mViewFinder );
        parent.addView( mViewFinder, 0);
        mViewFinder.setSurfaceTexture( output.getSurfaceTexture() );
        mBufferRotation = output.getRotationDegrees();
        int rotation = getDisplaySurfaceRotation( mViewFinder.getDisplay() );
        updateTransform( mViewFinder, rotation, output.getTextureSize(), mViewFinderDimens );
    }

    private Integer getDisplaySurfaceRotation( Display display ){
        Integer displayRotation = null;
        switch ( display.getRotation() ){
            case Surface.ROTATION_0 :
                displayRotation = 0;
                break;
            case Surface.ROTATION_90 :
                displayRotation = 90;
                break;
            case Surface.ROTATION_180 :
                displayRotation = 180;
                break;
            case Surface.ROTATION_270 :
                displayRotation = 270;
                break;
        }
        return displayRotation;
    }

    @Override
    public void onDisplayAdded( int displayId ) {}

    @Override
    public void onDisplayRemoved( int displayId ) {}

    @Override
    public void onDisplayChanged( int displayId ) {
        if ( mViewFinder == null ){
            return;
        }
        if ( displayId == mViewFinderDisplay ) {
            Display display = mDisplayManager.getDisplay( displayId );
            Integer rotation = getDisplaySurfaceRotation( display );
            updateTransform( mViewFinder, rotation, mBufferDimens, mViewFinderDimens );
        }
    }
}
