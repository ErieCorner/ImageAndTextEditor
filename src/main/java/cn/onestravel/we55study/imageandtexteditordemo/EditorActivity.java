package cn.onestravel.we55study.imageandtexteditordemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import view.OperateView;
import view.PartingLineView;
import view.PhotoContentView;
import view.TextContentView;
import view.TypePop;
import view.utils.MyGlideEngine;
import view.utils.ViewTypeEnum;


public class EditorActivity extends Activity implements TypePop.onViewItemClickListener, OperateView.onViewClickListener {
    private static final int REQUEST_PERMISSION = 100;//定义申请权限常量
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    private static String TAG = "EditorActivity.class";
    @BindView(R.id.inner_view)
    LinearLayout inner_view;
    @BindView(R.id.content_container)
    LinearLayout contentContainers;
    @BindView(R.id.tv_add_content)
    TextView tvAddContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private TypePop typePop;
    private boolean insert;
    private int insertPhonePosition;

    private Unbinder mUnbinder;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mUnbinder = ButterKnife.bind(this);
        typePop = new TypePop(this);
        tvAddContent = ((TextView) findViewById(R.id.tv_add_content));
        contentContainers = ((LinearLayout) findViewById(R.id.content_container));
        scrollView = ((ScrollView) findViewById(R.id.scrollView));
        inner_view = ((LinearLayout) findViewById(R.id.inner_view));

    }

    /**
     * 获取控件个数
     *
     * @return
     */
    private int getViewCount() {
        if (contentContainers != null) {
            return contentContainers.getChildCount();
        }
        return 0;
    }

    /**
     * 添加文本编辑框
     */
    private void addTxtView(int position) {
        if (contentContainers != null) {
            final TextContentView textContentView = new TextContentView(this, getViewCount());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            textContentView.setLayoutParams(params);
            textContentView.setEditHint("请填写内容");
            textContentView.setOnViewClickListener(this);
            contentContainers.addView(textContentView, position);
        }
    }

    @OnClick({R.id.tv_add_content})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_content:
//                显示底部弹框选择要添加的内容
                if (typePop != null) {
                    typePop.showPop();
                } else {
                    typePop = new TypePop(EditorActivity.this);
                    typePop.showPop();
                }
                typePop.setOnViewItemClickListener(this);
                break;
            default:
                break;
        }
    }


    @Override
    public void onViewItemClick(int viewType) {
        switch (viewType) {
            case TypePop.VIEW_TYPE_TXT:
                addTxtView(getViewCount());
                scrollToBottom(scrollView, inner_view);
                break;
            case TypePop.VIEW_TYPE_CAMERA:
                //todo

                break;
            case TypePop.VIEW_TYPE_DIVIDE_LINE:
                addDivideView(getViewCount());
                break;
            case TypePop.VIEW_TYPE_PHOTO:
                //调用图库选择图图片
                insert = false;
                getPomissions();
                break;
        }

    }

    /**
     * 添加分割线view
     */
    private void addDivideView(int position) {
        if (contentContainers != null) {
            PartingLineView divideView = new PartingLineView(this, getViewCount());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            divideView.setLayoutParams(params);
            divideView.setOnViewClickListener(this);
            contentContainers.addView(divideView, position);
        }
    }

    /**
     * 获取读取图片的权限
     */
    private void getPomissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int writeStorageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int openCameraResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (writeStorageResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditorActivity.this, "检测到您已拒绝\"读写存储空间权限\"或因系统问题申请失败，请前往\"权限管理\"中手动授权", Toast.LENGTH_SHORT);
                    return;
                }
                if (openCameraResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(EditorActivity.this, "检测到您已拒绝\"相机权限\"或因系统问题申请失败，请前往\"权限管理\"中手动授权", Toast.LENGTH_SHORT);
                    return;
                }
                getPhotos();
                break;
            default:
                break;
        }
    }

    /**
     * 调用图库获取图片
     */
    private void getPhotos() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))//照片视频全部显示MimeType.allOf()
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(9)//最大选择数量为9
                //.addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))//图片显示表格的大小
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//图像选择和预览活动所需的方向
                .thumbnailScale(0.85f)//缩放比例
                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new MyGlideEngine())//图片加载方式，Glide4需要自定义实现
                .capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                .captureStrategy(new CaptureStrategy(true, "com.sendtion.matisse.fileprovider"))//存储到哪里
                .forResult(REQUEST_CODE_CHOOSE);//请求码
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    if (index > -1) {
                        data = cursor.getString(0);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_CODE_CHOOSE) {
                    Log.i(TAG, "选择图片成功！开始上传……");
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    for (int i = 0; i < mSelected.size(); i++) {
                        String imgPath = getRealFilePathFromUri(this, mSelected.get(i));
                        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                        //添加图片view并显示图片
                        if (insert) {
                            addPhoneView(bitmap, insertPhonePosition + i);
                        } else {
                            addPhoneView(bitmap, contentContainers.getChildCount());
                        }
                    }
                    //插入图片后滑到底部
                    scrollToBottom(scrollView, inner_view);
                }
            }
        }
    }

    /**
     * 添加图片view
     *
     * @param bitmap 要显示的图片
     */
    private void addPhoneView(Bitmap bitmap, int position) {
        if (contentContainers != null) {
            PhotoContentView photoContentView = new PhotoContentView(this, getViewCount());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            photoContentView.setLayoutParams(params);
            photoContentView.setBitmapImg(bitmap);
            photoContentView.setOnViewClickListener(this);
            contentContainers.addView(photoContentView, position);
        }
    }

    /**
     * 滑到已填加view的底部
     *
     * @param scroll
     * @param inner
     */
    public void scrollToBottom(final View scroll, final View inner) {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scroll == null || inner == null) {
                    return;
                }
                int offset = inner.getMeasuredHeight() - scroll.getHeight();
                if (offset < 0) {
                    offset = 0;
                }

                scroll.scrollTo(0, offset);
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();

    }

    @Override
    public void onOperateViewClick(int type, View view, ViewTypeEnum viewType) {
        String msg = "";
        int position = 0;
        switch (type) {
            case OperateView.OPERATE_TYPE_DELETE:
                //删除
                position = contentContainers.indexOfChild(view);
                if (getViewCount() > 0) {
                    if (position == 0 && getViewCount() >= 2) {
                        View bottomView = contentContainers.getChildAt(1);
                        if (bottomView instanceof TextContentView) {
                            ((TextContentView) bottomView).setViewPosition(0);
                        } else if (bottomView instanceof PhotoContentView) {
                            ((PhotoContentView) bottomView).setViewPosition(0);
                        } else if (bottomView instanceof PartingLineView) {
                            ((PartingLineView) bottomView).setViewPosition(0);
                        }
                    }
                    contentContainers.removeView(view);
                }
                break;
            case OperateView.OPERATE_TYPE_INSERT:
                //插入
                if (ViewTypeEnum.TXT_VIEW == viewType) {
                    addTxtView(contentContainers.indexOfChild(view) + 1);
                } else if (ViewTypeEnum.PHOTO_VIEW == viewType) {
                    insert = true;
                    insertPhonePosition = contentContainers.indexOfChild(view) + 1;
                    getPhotos();
                } else if (ViewTypeEnum.DEVIDE_LINE_VIEW == viewType) {
                    addDivideView(contentContainers.indexOfChild(view) + 1);
                }
                break;
            case OperateView.OPERATE_TYPE_MOVE_UP:
                //上移
                position = contentContainers.indexOfChild(view);
                if (ViewTypeEnum.TXT_VIEW == viewType) {
                    if (position > 0) {
                        View topView = contentContainers.getChildAt(position - 1);
                        if (topView instanceof TextContentView) {
                            ((TextContentView) topView).setViewPosition(position);
                        } else if (topView instanceof PhotoContentView) {
                            ((PhotoContentView) topView).setViewPosition(position);
                        } else if (topView instanceof PartingLineView) {
                            ((PartingLineView) topView).setViewPosition(position);
                        }
                        contentContainers.removeView(view);
                        contentContainers.addView(view, position - 1);
                        ((TextContentView) view).setViewPosition(position - 1);
                    }

                } else if (ViewTypeEnum.PHOTO_VIEW == viewType) {
                    if (position > 0) {
                        View topView = contentContainers.getChildAt(position - 1);
                        if (topView instanceof TextContentView) {
                            ((TextContentView) topView).setViewPosition(position);
                        } else if (topView instanceof PhotoContentView) {
                            ((PhotoContentView) topView).setViewPosition(position);

                        } else if (topView instanceof PartingLineView) {
                            ((PartingLineView) topView).setViewPosition(position);
                        }
                        contentContainers.removeView(view);
                        contentContainers.addView(view, position - 1);
                        ((PhotoContentView) view).setViewPosition(position - 1);
                    }
                } else if (ViewTypeEnum.DEVIDE_LINE_VIEW == viewType) {
                    if (position > 0) {
                        View topView = contentContainers.getChildAt(position - 1);
                        if (topView instanceof TextContentView) {
                            ((TextContentView) topView).setViewPosition(position);
                        } else if (topView instanceof PhotoContentView) {
                            ((PhotoContentView) topView).setViewPosition(position);

                        } else if (topView instanceof PartingLineView) {
                            ((PartingLineView) topView).setViewPosition(position);
                        }
                        contentContainers.removeView(view);
                        contentContainers.addView(view, position - 1);
                        ((PartingLineView) view).setViewPosition(position - 1);
                    }
                }
                break;
            default:
                break;
        }
    }


}
