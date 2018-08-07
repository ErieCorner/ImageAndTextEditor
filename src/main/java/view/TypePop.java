package view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import cn.onestravel.we55study.imageandtexteditordemo.R;


/**
 * created by Sunxiaxia on 2018/7/5 15:28
 * description:选择添加内容类型的底部弹框
 */
public class TypePop extends PopupWindow implements View.OnClickListener {
    public static final int VIEW_TYPE_TXT = 101;
    public static final int VIEW_TYPE_PHOTO = 102;
    public static final int VIEW_TYPE_CAMERA = 103;
    public static final int VIEW_TYPE_DIVIDE_LINE = 104;
    private Context mContext;
    private onViewItemClickListener onViewItemClickListener;
    private LinearLayout txtView;
    private LinearLayout photoView;
    private LinearLayout cameraView;
    private LinearLayout divideLineView;

    public TypePop(Context context) {
        super(context);
        mContext = context;
        initView();
    }


    /**
     * 初始化布局及属性
     */
    private void initView() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.typepop_layout, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dimissPop();
            }
        });
        setContentView(contentView);
        txtView = ((LinearLayout) contentView.findViewById(R.id.txt_ll));
        photoView = ((LinearLayout) contentView.findViewById(R.id.photo_ll));
        cameraView = ((LinearLayout) contentView.findViewById(R.id.camera_ll));
        divideLineView = ((LinearLayout) contentView.findViewById(R.id.divide_line_ll));
        txtView.setOnClickListener(this);
        photoView.setOnClickListener(this);
        cameraView.setOnClickListener(this);
        divideLineView.setOnClickListener(this);
    }

    /**
     * 从屏幕底部弹出
     */
    public void showPop() {
        if (((Activity) mContext).getWindow().isActive()) {
            showAtLocation(((Activity) mContext).getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);

        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (((Activity) mContext).getWindow().isActive()) {
                        showAtLocation(((Activity) mContext).getWindow().getDecorView(),
                                Gravity.BOTTOM, 0, 0);
                    }
                }
            }, 600);
        }

    }

    /**
     * @param listener 设置view监听事件
     */
    public void setOnViewItemClickListener(onViewItemClickListener listener) {
        this.onViewItemClickListener = listener;
    }

    /**
     * 隐藏popWindow
     */
    public void dimissPop() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_ll:
                if (onViewItemClickListener != null) {
                    onViewItemClickListener.onViewItemClick(VIEW_TYPE_TXT);
                    dimissPop();
                }
                break;
            case R.id.photo_ll:
                if (onViewItemClickListener != null) {
                    onViewItemClickListener.onViewItemClick(VIEW_TYPE_PHOTO);
                    dimissPop();
                }
                break;
            case R.id.camera_ll:
                if (onViewItemClickListener != null) {
                    onViewItemClickListener.onViewItemClick(VIEW_TYPE_CAMERA);
                    dimissPop();
                }
                break;
            case R.id.divide_line_ll:
                if (onViewItemClickListener != null) {
                    onViewItemClickListener.onViewItemClick(VIEW_TYPE_DIVIDE_LINE);
                    dimissPop();
                }
                break;
            default:
                break;

        }

    }

    /**
     * view点击事件
     */
    public interface onViewItemClickListener {
        public void onViewItemClick(int viewType);
    }
}
