package com.example.study.android.androidscreening.ui;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.study.android.androidscreening.R;
import com.example.study.android.androidscreening.adapter.ScreeningListAdapter;
import com.example.study.android.androidscreening.model.AttrList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * PopupWindow的布局
 */

public class RightSideslipChildLay extends FrameLayout {
    private List<AttrList.Attr.Vals> mVals_data = new ArrayList<AttrList.Attr.Vals>(); // 初始化的数据
    private ListView mBrandList;
    private ScreeningListAdapter mAdapter;
    private Context mCtx;
    private List<AttrList.Attr.Vals> selectBrandData;
    private ImageView meunBackIm;
    private TextView meunOkTv;

    /**
     *
     * @param context 上下文
     * @param mVals_data 初始化数据
     * @param Fristlist 已选中数据
     */
    public RightSideslipChildLay(Context context, List<AttrList.Attr.Vals> mVals_data, List<AttrList.Attr.Vals> Fristlist) {
        super(context);
        mCtx = context;
        this.mVals_data = mVals_data;
        selectBrandData = new ArrayList<AttrList.Attr.Vals>();
        initView(mVals_data, Fristlist);
    }

    //初始化view
    private void initView(List<AttrList.Attr.Vals> datas, List<AttrList.Attr.Vals> Fristlist) {
        View.inflate(getContext(), R.layout.include_right_sideslip_child_layout, this);
        mBrandList = (ListView) findViewById(R.id.select_brand_list);
        meunBackIm = (ImageView) findViewById(R.id.select_brand_back_im); // 返回
        meunOkTv = (TextView) findViewById(R.id.select_brand_ok_tv); // 确定
        meunBackIm.setOnClickListener(ClickListener);
        meunOkTv.setOnClickListener(ClickListener);
        // 初始化数据  已选中的数据
        setupList(datas, Fristlist);
    }

    /**
     * 设置默认选中的CheckBox的状态(原数据与已选中的合并)
     * @param mBrand_data 初始化数据
     * @param Fristlist 已选中的数据
     */
    private void setupList(List<AttrList.Attr.Vals> mBrand_data, List<AttrList.Attr.Vals> Fristlist) {
        if (mBrand_data != null && mBrand_data.size() > 0) {
            for (int i = 0; i < mBrand_data.size(); i++) {
                if (Fristlist != null && Fristlist.size() > 0) {
                    for (int j = 0; j < Fristlist.size(); j++) {
                        if (mBrand_data.get(i).getV().equals(Fristlist.get(j).getV())) {
                            mBrand_data.get(i).setChick(Fristlist.get(j).isChick());
                        }
                    }
                } else {
                    // 如果已选中的数据为0则设置所有的选项为未选中
                    mBrand_data.get(i).setChick(false);
                }

            }
            selectBrandData.clear();
            setupSelectData(mBrand_data);
            if (mAdapter == null) {
                mAdapter = new ScreeningListAdapter(mCtx, mBrand_data); // 注意这的传值
            } else {
                mAdapter.clear();
                mAdapter.addAll(mBrand_data);
            }
            mBrandList.setAdapter(mAdapter);

            mAdapter.setClickBack(new ScreeningListAdapter.ClickBack() {
                @Override
                public void setupClick() { // 保存popupwindow页面选中的值
                    setupSelectData(mAdapter.getData());
                }
            });

        }

    }

    // 设置选中的CheckBox list
    private void setupSelectData(List<AttrList.Attr.Vals> mList) {
        //Toast.makeText(mCtx, "size=" + mList.size(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).isChick()) {
                selectBrandData.add(mList.get(i)); //TODO 选中会多次添加
            } else {
                selectBrandData.remove(mList.get(i));
            }
        }
        selectBrandData = removeDuplicate(selectBrandData);
        //Toast.makeText(mCtx, "size=====" + selectBrandData.size(), Toast.LENGTH_SHORT).show();
    }

    private OnClickListenerWrapper ClickListener = new OnClickListenerWrapper() {
        @Override
        protected void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.select_brand_back_im:
                    if (meanCallBack != null){
                        meanCallBack.isDisMess(true, null,"");
                    }
                    break;
                case R.id.select_brand_ok_tv:
                    if (meanCallBack != null){
                        meanCallBack.isDisMess(true, mVals_data,setUpValsStrs(removeDuplicate(selectBrandData)));
                    }
                    break;
                default:
                    break;
            }
        }
    };
    //去除重复数据
    public List<AttrList.Attr.Vals> removeDuplicate(List<AttrList.Attr.Vals> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 拼接选中项的字符串
     * @param data
     * @return
     */
    private String setUpValsStrs(List<AttrList.Attr.Vals> data) {
        StringBuilder builder = new StringBuilder();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (data.size() == 1) {
                    builder.append(data.get(i).getV());
                } else {
                    if (i == data.size() - 1) {
                        builder.append(data.get(i).getV());
                    } else {
                        builder.append(data.get(i).getV() + ",");
                    }
                }

            }
            return new String(builder);
        } else {
            return "";
        }

    }
    public interface onMeanCallBack {
        void isDisMess(boolean isDis, List<AttrList.Attr.Vals> data,String str);
    }

    private onMeanCallBack meanCallBack;

    public void setOnMeanCallBack(onMeanCallBack m) {
        meanCallBack = m;
    }


}
