package com.changshagaosu.roadtools.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changshagaosu.roadtools.R;
import com.changshagaosu.roadtools.bean.DeaseItem;

public class ItemAdapter extends BaseAdapter {
	private List<DeaseItem> addressList;
	private LayoutInflater mInflater;

	public ItemAdapter(Context c, List<DeaseItem> appList) {
		addressList = appList;
		mInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void clear() {
		if (addressList != null) {
			addressList.clear();
		}
	}

	public int getCount() {
		return addressList.size();
	}

	public Object getItem(int position) {
		return addressList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		RecentViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_sub, null);
			holder = new RecentViewHolder();
			holder.codeText = (TextView) convertView
					.findViewById(R.id.text_code);
			holder.unitText = (TextView) convertView
					.findViewById(R.id.text_unit);
			holder.nameText = (TextView) convertView
					.findViewById(R.id.text_name);
			convertView.setTag(holder);
		} else {
			holder = (RecentViewHolder) convertView.getTag();
		}
		DeaseItem address = addressList.get(position);
		if (address != null) {
			holder.unitText.setText(address.getDTypeUnit());
			holder.nameText.setText(address.getDTypeName());
			holder.codeText.setText(address.getDTypeNumber());
		}
		return convertView;
	}

	private class RecentViewHolder {
		TextView codeText;
		TextView unitText;
		TextView nameText;
	}
}