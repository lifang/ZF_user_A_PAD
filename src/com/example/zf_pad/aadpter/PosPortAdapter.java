package com.example.zf_pad.aadpter;

import java.util.ArrayList;
import java.util.List;

import com.example.zf_pad.MyApplication;
import com.example.zf_pad.R;
import com.example.zf_pad.entity.PosPortChild;
import com.example.zf_pad.entity.PostPortEntity;
import com.example.zf_pad.entity.TestEntitiy;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PosPortAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<PostPortEntity> list;
	private List<PostPortEntity> glist = new ArrayList<PostPortEntity>();
	private LayoutInflater inflater;
	public ExpandableListView listView;

	public PosPortAdapter(Context context, List<PostPortEntity> list) {
		this.context = context;
		this.list = list;
		PostPortEntity ppe1 = new PostPortEntity();
		PostPortEntity ppe2 = new PostPortEntity();
		PostPortEntity ppe3 = new PostPortEntity();
		glist.add(ppe1);
		glist.add(ppe2);
		glist.add(ppe3);
		glist.get(0).getChildlist().get(0).setSeleck(true);

		for (int i = 0; i < list.size(); i++) {
			list.get(i).getChildlist().remove(0);
			list.get(i).getChildlist().remove(0);
			list.get(i).getChildlist().remove(0);
			list.get(i).getChildlist().remove(0);
		}

		// Toast.makeText(context, +list.get(0).getChildlist().size()+"",
		// 1000).show();
	}

	public void setListView(ExpandableListView listView) {
		this.listView = listView;
	}

	@Override
	public int getGroupCount() {
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return (int) Math.ceil(((double) list.get(groupPosition).getChildlist()
				.size()) / 4);
	}

	@Override
	public Object getGroup(int groupPosition) {
		return list.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return list.get(groupPosition).getChildlist().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final int childSize = (int) Math.ceil(((double) list.get(groupPosition)
				.getChildlist().size()) / 4);
		final int childListSize = list.get(groupPosition).getChildlist().size() - 1;
		inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.pos_port_item, null);
		TextView text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView text3 = (TextView) convertView.findViewById(R.id.text3);
		TextView text4 = (TextView) convertView.findViewById(R.id.text4);
		 CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.item_cb1);
		 CheckBox cb2 = (CheckBox) convertView.findViewById(R.id.item_cb2);
		CheckBox cb3 = (CheckBox) convertView.findViewById(R.id.item_cb3);
		CheckBox cb4 = (CheckBox) convertView.findViewById(R.id.item_cb4);

		if (childPosition == childSize - 1) {
			// Toast.makeText(context, +childSize-1+"", 1000).show();
			if (childListSize - childPosition * 4 == 0) {
				text1.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4).getTitile());
				text2.setVisibility(View.INVISIBLE);
				text3.setVisibility(View.INVISIBLE);
				text4.setVisibility(View.INVISIBLE);
				cb2.setVisibility(View.INVISIBLE);
				cb3.setVisibility(View.INVISIBLE);
				cb4.setVisibility(View.INVISIBLE);
			} else if (childListSize - childPosition * 4 == 1) {
				text1.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4).getTitile());
				text2.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 1).getTitile());
				text3.setVisibility(View.INVISIBLE);
				text4.setVisibility(View.INVISIBLE);
				cb3.setVisibility(View.INVISIBLE);
				cb4.setVisibility(View.INVISIBLE);
			} else if (childListSize - childPosition * 4 == 2) {
				text1.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4).getTitile());
				text2.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 1).getTitile());
				text3.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 2).getTitile());
				text4.setVisibility(View.INVISIBLE);
				cb4.setVisibility(View.INVISIBLE);
			} else if (childListSize - childPosition * 4 == 3) {
				text1.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4).getTitile());
				text2.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 1).getTitile());
				text3.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 2).getTitile());
				text4.setText(list.get(groupPosition).getChildlist()
						.get(childPosition * 4 + 3).getTitile());
			}

		} else {

			text1.setText(list.get(groupPosition).getChildlist()
					.get(childPosition * 4).getTitile());
			text2.setText(list.get(groupPosition).getChildlist()
					.get(childPosition * 4 + 1).getTitile());
			text3.setText(list.get(groupPosition).getChildlist()
					.get(childPosition * 4 + 2).getTitile());
			text4.setText(list.get(groupPosition).getChildlist()
					.get(childPosition * 4 + 3).getTitile());
		}

		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (arg1) {
					list.get(groupPosition).getChildlist()
							.get(childPosition * 4).setSeleck(true);
				} else {
					list.get(groupPosition).getChildlist()
							.get(childPosition * 4).setSeleck(false);
				}

			}
		});

		cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (childPosition == childSize - 1) {
					if (childListSize - childPosition * 4 <= 0)
						return;
				}
				if (arg1) {
					list.get(groupPosition).getChildlist()
							.get(childPosition * 4 + 1).setSeleck(true);
					// Toast.makeText(context, +childPosition*4+1+"被选",
					// 1000).show();
				} else {
					list.get(groupPosition).getChildlist()
							.get(childPosition * 4 + 1).setSeleck(false);
				}

			}
		});

		cb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (childPosition == childSize - 1) {

					if (childListSize - childPosition * 4 <= 1)
						return;
				} else {

				}
				if (arg1) {
					list.get(groupPosition).getChildlist()
							.get(childPosition * 4 + 2).setSeleck(true);
					// Toast.makeText(context, +childPosition*4+2+"被选",
					// 1000).show();
				} else {
					list.get(groupPosition).getChildlist()
							.get(childPosition * 4 + 2).setSeleck(false);
				}

			}
		});

		cb4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (childPosition == childSize - 1) {
					if (childListSize - childPosition * 4 <= 2)
						return;
				}
				if (arg1) {
					list.get(groupPosition).getChildlist()
							.get(childPosition * 4 + 3).setSeleck(true);
				} else {
					list.get(groupPosition).getChildlist()
							.get(childPosition * 4 + 3).setSeleck(false);
				}

			}
		});
		// 判断选项是否选中

		/*
		 * if (list.get(groupPosition).getChildlist().get(childPosition)
		 * .isSeleck()) { cb1.setChecked(true); Toast.makeText(context,
		 * +childPosition + "2222", 1000).show(); } if
		 * (list.get(groupPosition).getChildlist().get(childPosition + 1)
		 * .isSeleck()) { cb2.setChecked(true); Toast.makeText(context,
		 * +childPosition + 1 + "3333", 1000).show(); } if
		 * (list.get(groupPosition).getChildlist().get(childPosition + 2)
		 * .isSeleck()) { cb3.setChecked(true); Toast.makeText(context,
		 * +childPosition + 2 + "44444", 1000).show(); } if
		 * (list.get(groupPosition).getChildlist().get(childPosition + 3)
		 * .isSeleck()) { cb4.setChecked(true); }
		 */
		for (int i = 0; i < list.get(groupPosition).getChildlist().size(); i++) {
			if (list.get(groupPosition).getChildlist().get(i).isSeleck()) {
				int index = i / 4;
				if (childPosition == index) {
					int indexof = i - index * 4;
					switch (indexof) {
					case 0:
						cb1.setChecked(true);
						break;
					case 1:
						cb2.setChecked(true);
						break;
					case 2:
						cb3.setChecked(true);
						break;
					case 3:
						cb4.setChecked(true);
						break;
					default:
						break;
					}
				}
			}
		}
		return convertView;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, ViewGroup parent) {
		inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.pos_port_parent_item, null);

		TextView tv = (TextView) convertView.findViewById(R.id.tv1);
		CheckBox cba = (CheckBox) convertView.findViewById(R.id.item_cb_all);
		TextView text1 = (TextView) convertView.findViewById(R.id.text1);
		TextView text2 = (TextView) convertView.findViewById(R.id.text2);
		TextView text3 = (TextView) convertView.findViewById(R.id.text3);
		TextView text4 = (TextView) convertView.findViewById(R.id.text4);
		final CheckBox cb1 = (CheckBox) convertView.findViewById(R.id.item_cb1);
		final CheckBox cb2 = (CheckBox) convertView.findViewById(R.id.item_cb2);
		final CheckBox cb3 = (CheckBox) convertView.findViewById(R.id.item_cb3);
		final CheckBox cb4 = (CheckBox) convertView.findViewById(R.id.item_cb4);

		// Toast.makeText(context, +childSize-1+"", 1000).show();
		if (glist.get(groupPosition).getChildlist().size() == 0) {

			text1.setVisibility(View.INVISIBLE);
			text2.setVisibility(View.INVISIBLE);
			text3.setVisibility(View.INVISIBLE);
			text4.setVisibility(View.INVISIBLE);
			cb1.setVisibility(View.INVISIBLE);
			cb2.setVisibility(View.INVISIBLE);
			cb3.setVisibility(View.INVISIBLE);
			cb4.setVisibility(View.INVISIBLE);
		} else if (glist.get(groupPosition).getChildlist().size() == 1) {
			text1.setText(glist.get(groupPosition).getChildlist().get(0)
					.getTitile());
			text2.setVisibility(View.INVISIBLE);
			text3.setVisibility(View.INVISIBLE);
			text4.setVisibility(View.INVISIBLE);
			cb2.setVisibility(View.INVISIBLE);
			cb3.setVisibility(View.INVISIBLE);
			cb4.setVisibility(View.INVISIBLE);
		} else if (glist.get(groupPosition).getChildlist().size() == 2) {
			text1.setText(glist.get(groupPosition).getChildlist().get(0)
					.getTitile());
			text2.setText(glist.get(groupPosition).getChildlist().get(1)
					.getTitile());
			text3.setVisibility(View.INVISIBLE);
			cb3.setVisibility(View.INVISIBLE);
			text4.setVisibility(View.INVISIBLE);
			cb4.setVisibility(View.INVISIBLE);
		} else if (glist.get(groupPosition).getChildlist().size() == 3) {
			text1.setText(glist.get(groupPosition).getChildlist().get(0)
					.getTitile());
			text2.setText(glist.get(groupPosition).getChildlist().get(1)
					.getTitile());
			text1.setText(glist.get(groupPosition).getChildlist().get(2)
					.getTitile());
			text4.setVisibility(View.INVISIBLE);
			cb4.setVisibility(View.INVISIBLE);

		} else {
			text1.setText(glist.get(groupPosition).getChildlist().get(0)
					.getTitile());
			text2.setText(glist.get(groupPosition).getChildlist().get(1)
					.getTitile());
			text3.setText(glist.get(groupPosition).getChildlist().get(2)
					.getTitile());
			text4.setText(glist.get(groupPosition).getChildlist().get(3)
					.getTitile());

		}
		cba.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					for (PosPortChild ppc : list.get(groupPosition)
							.getChildlist()) {
						ppc.setSeleck(true);

					}
					for (PosPortChild ppc : glist.get(groupPosition)
							.getChildlist()) {
						ppc.setSeleck(true);

					}
					list.get(groupPosition).setSeleck(true);
				} else {
					for (PosPortChild ppc : list.get(groupPosition)
							.getChildlist()) {
						ppc.setSeleck(false);
					}
					for (PosPortChild ppc : glist.get(groupPosition)
							.getChildlist()) {
						ppc.setSeleck(false);

					}

					list.get(groupPosition).setSeleck(false);
				}
				PosPortAdapter.this.notifyDataSetChanged();
			}
		});
		if (list.get(groupPosition).isSeleck())
			cba.setChecked(true);
		tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isExpanded) {
					listView.collapseGroup(groupPosition);
				} else {
					listView.expandGroup(groupPosition);
				}

			}
		});
		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					glist.get(groupPosition).getChildlist().get(0)
							.setSeleck(true);
					
				} else {
					glist.get(groupPosition).getChildlist().get(0)
							.setSeleck(false);
				}

			}
		});

		cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					glist.get(groupPosition).getChildlist().get(1)
							.setSeleck(true);
				} else {
					glist.get(groupPosition).getChildlist().get(1)
							.setSeleck(false);
				}

			}
		});

		cb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					glist.get(groupPosition).getChildlist().get(2)
							.setSeleck(true);
				} else {
					glist.get(groupPosition).getChildlist().get(2)
							.setSeleck(false);
				}

			}
		});

		cb4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					glist.get(groupPosition).getChildlist().get(3)
							.setSeleck(true);
				} else {
					glist.get(groupPosition).getChildlist().get(3)
							.setSeleck(false);
				}

			}
		});
		

	for (int i = 0; i < glist.get(groupPosition).getChildlist().size(); i++) {
		if (glist.get(groupPosition).getChildlist().get(i).isSeleck()) {
		
				switch (i) {
				case 0:
					cb1.setChecked(true);
					
					break;
				case 1:
					cb2.setChecked(true);
					break;
				case 2:
					cb3.setChecked(true);
					break;
				case 3:
					cb4.setChecked(true);
					break;
				default:
					break;
				
			}
		}
	}
		return convertView;
	}
}
