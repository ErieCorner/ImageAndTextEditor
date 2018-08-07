package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import cn.onestravel.we55study.imageandtexteditordemo.R;
import view.utils.ViewTypeEnum;


/**
 * created by Sunxiaxia on 2018/7/5 18:33
 * description:编辑文本的view自定义
 */
public class TextContentView extends RelativeLayout implements OperateView.onViewClickListener {

    private EditText editView;
    private OperateView operateView;
    private int position;//记录此view显示的位置
    private OperateView.onViewClickListener mOnViewClickListener;

    public TextContentView(Context context, int pos) {
        super(context);
        this.position = pos;
        initView(context, null, 0);
    }

    public TextContentView(Context context, int pos, AttributeSet attrs) {
        super(context, attrs);
        this.position = pos;
        initView(context, attrs, 0);
    }


    public TextContentView(Context context, int pos, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.position = pos;
        initView(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.txt_content_item_layout, null);
        editView = ((EditText) view.findViewById(R.id.edit_content));
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
     * 获取添加的view的类型
     *
     * @return
     */
    public ViewTypeEnum getViewType() {
        return ViewTypeEnum.TXT_VIEW;
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


    /**
     * @return 获取文本内容
     */
    public String getTxtContent() {
        if (editView != null) {
            return editView.getText().toString();
        }
        return "";
    }

    /**
     * 设置输入文本框中的提示内容
     *
     * @param charSequence 提示内容
     */
    public void setEditHint(CharSequence charSequence) {
        if (editView != null) {
            editView.setHint(charSequence);
        }
    }

    /**
     * 设置输入文本框中的提示内容
     *
     * @param resId 提示内容资源
     */
    public void setEditHint(int resId) {
        if (editView != null) {
            editView.setHint(resId);
        }
    }

    /**
     * 设置操作按钮的点击事件
     *
     * @param type
     * @param view
     * @param viewType
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
     * 设置字体大小
     *
     * @param resId
     */
    public void setTextSize(int resId) {
        if (editView != null) {
            editView.setTextSize(resId);
        }
    }

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        if (editView != null) {
            editView.setTextColor(color);
        }
    }

    /**
     * 设置最小行数
     *
     * @param lines
     */
    public void setEditViewMinLine(int lines) {
        if (editView != null) {
            editView.setMinLines(lines);
        }
    }

    /**
     * 设置最小行数
     *
     * @param height
     */
    public void setEditViewMinHeight(int height) {
        if (editView != null) {
            editView.setMinHeight(height);
        }
    }

    //todo 设置文本框样式、字体大小、颜色、提示语颜色、大小等

}
