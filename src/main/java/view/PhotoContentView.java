package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.onestravel.we55study.imageandtexteditordemo.R;
import view.utils.ViewTypeEnum;
/**
 * created by Sunxiaxia on 2018/7/6 18:19
 * description: 编辑图片内容的view
 */
public class PhotoContentView extends RelativeLayout implements OperateView.onViewClickListener {
    private int position;//view显示的位置
    private ImageView ivPhoto;
    private OperateView operateView;
    private OperateView.onViewClickListener mOnViewClickListener;

    public PhotoContentView(Context context, int pos) {
        super(context);
        this.position = pos;
        initView(context, null, 0);
    }


    public PhotoContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);

    }

    public PhotoContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    /**
     * 初始化子控件
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.photo_content_item_layout, null);
        ivPhoto = ((ImageView) view.findViewById(R.id.iv_photo));
        operateView = ((OperateView) view.findViewById(R.id.operate_view));
        operateView.setOnViewClickListener(this);
        addView(view);
        //控制上移按钮的显示
        if (position > 0) {
            operateView.setMoveUpTvVisible(true);
        } else {
            operateView.setMoveUpTvVisible(false);
        }
    }

    /**
     * 设置图片
     *
     * @param bitmap
     */
    public void setBitmapImg(Bitmap bitmap) {
        if (ivPhoto != null && bitmap != null) {
            ivPhoto.setImageBitmap(bitmap);
        }
    }

    /**
     * 操作事件监听
     *
     * @param type     操作的类型
     * @param view
     * @param viewType view 的类型
     */
    @Override
    public void onOperateViewClick(int type, View view, ViewTypeEnum viewType) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onOperateViewClick(type, this, viewType);
        }
    }

    /**
     * 设置view点击事件
     *
     * @param listener 点击事件
     */
    public void setOnViewClickListener(OperateView.onViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }


    /**
     * 设置view在父容器中的位置
     *
     * @param pos
     */
    public void setViewPosition(int pos) {
        this.position = pos;
        if (pos > 0) {
            operateView.setMoveUpTvVisible(true);
        } else {
            operateView.setMoveUpTvVisible(false);
        }
    }

}
