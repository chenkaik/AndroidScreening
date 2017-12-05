package com.example.study.android.androidscreening.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.study.android.androidscreening.R;
import com.example.study.android.androidscreening.model.AttrList;
import com.example.study.android.androidscreening.ui.OnClickListenerWrapper;

import java.util.HashSet;
import java.util.List;

/**
 * GridView适配器
 */

public class RightSideslipLayChildAdapter extends SimpleBaseAdapter<AttrList.Attr.Vals> implements CompoundButton.OnCheckedChangeListener {

    public List<AttrList.Attr.Vals> seachData;

    public void setSeachData(List<AttrList.Attr.Vals> seachData) {
        this.seachData = seachData;
    }

    public RightSideslipLayChildAdapter(Context context, List<AttrList.Attr.Vals> data, List<AttrList.Attr.Vals> selectVals) {
        // 九宫格值与选中的值
        super(context, data);
        this.seachData = selectVals;
    }

    @Override
    public int getItemResource() {
        return R.layout.gv_right_sideslip_child_layout;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        CheckBox checkBox = holder.getView(R.id.item_frameRb);
        final AttrList.Attr.Vals vals = getData().get(position);
        checkBox.setText(vals.getV()); // 设置具体的值
        checkBox.setTag(position); // 设置标记
        checkBox.setChecked(vals.isChick());
        if ("查看更多 >".equals(vals.getV())) {
            checkBox.setBackgroundResource(0);
            checkBox.setTextColor(checkBox.getResources().getColor(R.color.colorPrimary));
        }
        checkBox.setOnCheckedChangeListener(this);
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!buttonView.isPressed()){ // 区分开人为点击和setChecked，是否按下,否则当我setChecked()时会触发此listener
            return;
        }
        int position = (int) buttonView.getTag();
        final AttrList.Attr.Vals vals = getData().get(position);
        buttonView.setOnClickListener(new OnClickListenerWrapper() {
            @Override
            protected void onSingleClick(View v) {
                //Toast.makeText(context, "进来了", Toast.LENGTH_SHORT).show();
                if ("查看更多 >".equals(vals.getV())) {
                    if (mShowPopCallBack != null){
                        mShowPopCallBack.setupShowPopCallBack(seachData); // 点击查看更多准备回调popuwindow
                    }
                }
            }
        });

        if (isChecked) {
            if ("查看更多 >".equals(vals.getV())) {
                return;
            }
            vals.setChick(isChecked);
            seachData.add(vals);
        } else if (isChecked == false){
            if ("查看更多 >".equals(vals.getV())) {
                return;
            }
            vals.setChick(isChecked);
            seachData.remove(vals);
        }
        notifyDataSetChanged();
        if (slidLayFrameChildCallBack != null) { // 最终回调到第一个View进行刷新界面值显示
            //Toast.makeText(context, "" + seachData.size(), Toast.LENGTH_SHORT).show();
            slidLayFrameChildCallBack.CallBackSelectData(removeDuplicate(seachData));
        }
    }


    public void refresh(){
        seachData.clear();
        notifyDataSetChanged();
    }

    /**
     * 去除重复数据
     * @param list
     * @return
     */
    public List<AttrList.Attr.Vals> removeDuplicate(List<AttrList.Attr.Vals> list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    // 回调选中或者取消选中的值
    private SlidLayFrameChildCallBack slidLayFrameChildCallBack;

    public interface SlidLayFrameChildCallBack {
        void CallBackSelectData(List<AttrList.Attr.Vals> seachData);
    }

    public void setSlidLayFrameChildCallBack(SlidLayFrameChildCallBack slidLayFrameChildCallBack) {
        this.slidLayFrameChildCallBack = slidLayFrameChildCallBack;
    }


    // 回调popuwindow
    private ShowPopCallBack mShowPopCallBack;

    public interface ShowPopCallBack {
        void setupShowPopCallBack(List<AttrList.Attr.Vals> seachData);
    }

    public void setShowPopCallBack(ShowPopCallBack mShowPopCallBack) {
        this.mShowPopCallBack = mShowPopCallBack;
    }

}