package com.logisticsView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.adapter.LogisticsAdapter;
import com.bwelco.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义节点Listview
 * 
 * @author zad
 * 
 */
public class CustomNodeListView extends LinearLayout {

	private CustomNodeLineView nodeLineView;
	private CustomListView listView;
	private BaseAdapter adapter;
	private List<Float> nodeRadiusDistances;

	public CustomNodeListView(Context context) {
		this(context, null);
	}

	public CustomNodeListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressLint("NewApi")
	public CustomNodeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(
				R.layout.custom_node_listview_layout, this);
		this.nodeLineView = (CustomNodeLineView) this
				.findViewById(R.id.nodeLineView);
		this.listView = (CustomListView) this.findViewById(R.id.listView);
		this.listView.setEnabled(false);
		this.nodeRadiusDistances = new ArrayList<Float>();
	}

	public void setTopNodePaintStrokeWidth(float topNodePaintStrokeWidth) {
		if (this.nodeLineView != null) {
			this.nodeLineView
					.setTopNodePaintStrokeWidth(topNodePaintStrokeWidth);
		}
		invalidate();
	}

	public BaseAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter; 
		this.listView.setAdapter(adapter);
		this.nodeRadiusDistances = ((LogisticsAdapter) adapter).getNodeRadiusDistances();
		//最后一个用于添加listview底部分割线高度
		this.nodeRadiusDistances.set(this.nodeRadiusDistances.size() - 1, (float)this.listView.getDividerHeight());
		this.setNodeCount(adapter.getCount());
		this.setNodeRadiusDistances(nodeRadiusDistances);
		invalidate();
	}

	private void setNodeRadiusDistances(List<Float> nodeRadiusDistances) {
		this.nodeLineView.setNodeRadiusDistances(nodeRadiusDistances);
		invalidate();
	}

	private void setNodeCount(int nodeCount) {
		this.nodeLineView.setNodeCount(nodeCount);
		invalidate();
	}
	
	public void setItemPaddingTop(int itemPaddingTop) {
		this.nodeLineView.setItemPaddingTop(itemPaddingTop);
		invalidate();
	}
	
	public void addHeaderView(View view) {
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView(view, 0);
		invalidate();
	}
}
