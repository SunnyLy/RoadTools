package com.changshagaosu.roadtools.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.UserBean;
import com.changshagaosu.roadtools.callback.ITreeSelectCallback;
import com.changshagaosu.roadtools.ui.view.tree.TreeBaseAdapter;
import com.changshagaosu.roadtools.ui.view.tree.TreeNode;

import java.util.List;

/**
 * @Annotation <p>描述</p>
 * @Auth Sunny
 * @date 2017/9/8
 * @Version V1.0.0
 */

public class RepairProjectAdapter<T> extends TreeBaseAdapter {
    private LayoutInflater inflater = null;
    private ITreeSelectCallback mCallback;

    public void setmCallback(ITreeSelectCallback mCallback) {
        this.mCallback = mCallback;
    }

    public RepairProjectAdapter(Context context, List<T> dataList) {
        super(dataList, 30);
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        public ImageView imageView = null;
        public TextView textView = null;
        public CheckBox checkBox = null;
    }

    @Override
    public View getConvertView(final TreeNode node, int position,
                               View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater
                    .inflate(R.layout.treelistview_item, null);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageView);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.textView);
            holder.checkBox = (CheckBox) convertView
                    .findViewById(R.id.checkBox);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        boolean isGroup = node.isGroup();
        UserBean bean = (UserBean) node.getSelfData();
        if (isGroup == true) {
            holder.imageView.setImageResource(R.drawable.ic_launcher);
            // holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            holder.textView.setTextColor(Color.BLACK);
            holder.checkBox.setVisibility(View.GONE);
        } else {
            holder.imageView.setImageBitmap(null);
            //  holder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            holder.textView.setTextColor(Color.BLACK);
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        holder.textView.setText(bean.getName());

        holder.checkBox.setChecked(node.isSelected());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onItemSelected(node.getId(), !node.isSelected());
                }
                // mTreeListAdapter.select(node.getId(), !node.isSelected());
            }
        });

        return convertView;
    }

}
