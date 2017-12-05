package com.example.study.android.androidscreening.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study.android.androidscreening.R;
import com.example.study.android.androidscreening.model.AttrList;
import com.example.study.android.androidscreening.ui.OnClickListenerWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class ScreeningListAdapter extends SimpleBaseAdapter<AttrList.Attr.Vals> {
    private List<AttrList.Attr.Vals> chickList; // 存放已选中的值

    public ScreeningListAdapter(Context context, List<AttrList.Attr.Vals> data) {
        super(context, data);
        chickList = new ArrayList<AttrList.Attr.Vals>();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_right_sideslip_child_layout;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        TextView mubTv = holder.getView(R.id.brand_list_Tv);
        CheckBox brandCb = holder.getView(R.id.select_brand_cb);
        LinearLayout brandLay = holder.getView(R.id.select_brand_lay);
        final AttrList.Attr.Vals topBrand = getData().get(position);

        brandCb.setVisibility(topBrand.isChick() ? View.VISIBLE : View.GONE);
        mubTv.setText((topBrand.getV()));
        brandCb.setChecked(topBrand.isChick()); // 设置是否选中

        brandLay.setOnClickListener(new OnClickListenerWrapper() {
            @Override
            protected void onSingleClick(View v) {
                boolean isSelect = !topBrand.isChick();
                // 再将当前选择CB的实际状态
                topBrand.setChick(isSelect);
                notifyDataSetChanged();

                if (isSelect) {
                    if (removeDuplicate(getAdapterChick()).size() >= 10) {
                        Toast.makeText(context, "抱歉，最多只能选择10个", Toast.LENGTH_SHORT).show();
                        topBrand.setChick(false);
                        notifyDataSetChanged();
                        return;
                    } else {
                        removeDuplicate(getAdapterChick()).add(topBrand);
                    }

                } else {
                    removeDuplicate(getAdapterChick()).remove(topBrand);
                }
                if (mBack != null){
                    mBack.setupClick(); // 表示已经将值回传过去了
                }
            }

        });
        return convertView;
    }


    //去除重复数据
    public List<AttrList.Attr.Vals> removeDuplicate(List<AttrList.Attr.Vals> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 获取已选中的值
     * @return
     */
    public List<AttrList.Attr.Vals> getAdapterChick() {
        return chickList;
    }

    public ClickBack mBack;

    public interface ClickBack {
        void setupClick();
    }

    public void setClickBack(ClickBack mBack) {
        this.mBack = mBack;
    }

    public boolean isEmptyAfterTrim(String s) {
        return TextUtils.isEmpty(s) || TextUtils.isEmpty(s.trim()) || "null".equals(s);
    }
}
