package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.onestravel.we55study.imageandtexteditordemo.R;
import view.utils.ViewTypeEnum;


/**
 * created by Sunxiaxia on 2018/8/3 10:05
 * description:组合分割线view
 */
public class PartingLineView extends RelativeLayout implements OperateView.onViewClickListener {
    private int position;
    private OperateView operateView;
    private ImageView divideImg;
    private OperateView.onViewClickListener mOnViewClickListener;


    public PartingLineView(Context context, int pos) {
        super(context);
        this.position = pos;
        initView(context, null, 0);
    }


    public PartingLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public PartingLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.parting_item_layout, null);
        operateView = ((OperateView) view.findViewById(R.id.parting_operate));
        divideImg = ((ImageView) view.findViewById(R.id.divide_line_icon));
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
     * 设置分割线
     *
     * @param resId 分割线资源id
     */
    public void setDivideImg(int resId) {
        if (divideImg != null) {
            divideImg.setImageResource(resId);
        }
    }

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
