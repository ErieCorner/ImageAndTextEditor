package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.onestravel.we55study.imageandtexteditordemo.R;
import view.utils.ViewTypeEnum;

/**
 * created by Sunxiaxia on 2018/7/5 18:42
 * description:
 */
public class OperateView extends LinearLayout implements View.OnClickListener {
    public static final int OPERATE_TYPE_DELETE = 201;
    public static final int OPERATE_TYPE_INSERT = 202;
    public static final int OPERATE_TYPE_MOVE_UP = 203;
    private static final int USED_FOR_TXT = 1;//在编辑文本view中使用
    private static final int USED_FOR_PHOTO = 2;//在编辑图片view中使用
    private static final int USED_FOR_DIVIDE = 3;//在编辑分割线view中使用
    private int usedType = USED_FOR_TXT;

    private TextView deleteTv;
    private TextView moveUpTv;
    private TextView insertTv;
    private onViewClickListener mOnViewClickListener;

    public OperateView(Context context) {
        super(context);
        initView(context, null, 0);
    }


    public OperateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public OperateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.operate_view, null);
        deleteTv = ((TextView) view.findViewById(R.id.tv_delete));
        moveUpTv = ((TextView) view.findViewById(R.id.tv_move_up));
        insertTv = ((TextView) view.findViewById(R.id.tv_insert));
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OperateView);
        try {
            usedType = ta.getInt(R.styleable.OperateView_operateViewUsed, usedType);
        } finally {
            ta.recycle();
        }
        deleteTv.setOnClickListener(this);
        moveUpTv.setOnClickListener(this);
        insertTv.setOnClickListener(this);
        addView(view);
    }

    /**
     * 设置view点击事件
     *
     * @param listener 点击事件
     */
    public void setOnViewClickListener(onViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }

    /**
     * @param isVisible 是否可见
     *                  设置上移按钮可见
     */
    public void setMoveUpTvVisible(boolean isVisible) {
        if (moveUpTv != null) {
            if (isVisible) {
                moveUpTv.setVisibility(VISIBLE);
            } else {
                moveUpTv.setVisibility(GONE);
            }
        }
    }

//    /**
//     * @param lisenter 设置删除按钮监听
//     */
//    public void setOnDeleteViewClickLisenter(OnClickListener lisenter) {
//        deleteTv.setOnClickListener(lisenter);
//    }
//
//    /**
//     * @param lisenter view 的点击监听器
//     *                 设置上移按钮的点击监听事件
//     */
//    public void setOnMoveUpViewClickListener(OnClickListener lisenter) {
//        moveUpTv.setOnClickListener(lisenter);
//    }
//
//    /**
//     * @param lisenter view 的点击监听器
//     *                 设置插入按钮的点击监听事件
//     */
//    public void setOnInsertViewClickListener(OnClickListener lisenter) {
//        insertTv.setOnClickListener(lisenter);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                if (mOnViewClickListener != null) {
                    if (USED_FOR_TXT == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_DELETE, null, ViewTypeEnum.TXT_VIEW);
                    } else if (USED_FOR_PHOTO == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_DELETE, null, ViewTypeEnum.PHOTO_VIEW);
                    } else if (USED_FOR_DIVIDE == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_DELETE, null, ViewTypeEnum.DEVIDE_LINE_VIEW);
                    }
                }
                break;
            case R.id.tv_insert:
                if (mOnViewClickListener != null) {
                    if (USED_FOR_TXT == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_INSERT, null, ViewTypeEnum.TXT_VIEW);
                    } else if (USED_FOR_PHOTO == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_INSERT, null, ViewTypeEnum.PHOTO_VIEW);
                    } else if (USED_FOR_DIVIDE == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_INSERT, null, ViewTypeEnum.DEVIDE_LINE_VIEW);
                    }
                }
                break;
            case R.id.tv_move_up:
                if (mOnViewClickListener != null) {
                    if (USED_FOR_TXT == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_MOVE_UP, null, ViewTypeEnum.TXT_VIEW);
                    } else if (USED_FOR_PHOTO == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_MOVE_UP, null, ViewTypeEnum.PHOTO_VIEW);
                    } else if (USED_FOR_DIVIDE == usedType) {
                        mOnViewClickListener.onOperateViewClick(OPERATE_TYPE_MOVE_UP, null, ViewTypeEnum.DEVIDE_LINE_VIEW);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * view 点击事件
     */
    public interface onViewClickListener {
        public void onOperateViewClick(int type, View view, ViewTypeEnum viewType);
    }


}
