package view.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * 自定义Glide加载引擎，用于知乎图片选择器
 */
public class MyGlideEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int i, Drawable drawable, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions()
                        .override(i, i)
                        .placeholder(drawable)
                        .centerCrop())
                .into(imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int i, Drawable drawable, ImageView imageView, Uri uri) {
        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .apply(new RequestOptions()
                        .override(i, i)
                        .placeholder(drawable)
                        .centerCrop())
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, int i, int i1, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .override(i, i1)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    @Override
    public void loadGifImage(Context context, int i, int i1, ImageView imageView, Uri uri) {
        Glide.with(context)
                .load(uri)
                .apply(new RequestOptions()
                        .override(i, i1)
                        .priority(Priority.HIGH)
                        .fitCenter())
                .into(imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return true;
    }
}
