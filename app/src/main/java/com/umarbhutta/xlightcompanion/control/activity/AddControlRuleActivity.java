package com.umarbhutta.xlightcompanion.control.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.Logger;
import com.umarbhutta.xlightcompanion.Tools.StringUtil;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.Tools.UserUtils;
import com.umarbhutta.xlightcompanion.control.activity.condition.EntryConditionActivity;
import com.umarbhutta.xlightcompanion.control.activity.result.ControlRuseltActivity;
import com.umarbhutta.xlightcompanion.okHttp.model.Actioncmd;
import com.umarbhutta.xlightcompanion.okHttp.model.Actionnotify;
import com.umarbhutta.xlightcompanion.okHttp.model.Condition;
import com.umarbhutta.xlightcompanion.okHttp.model.CreateRuleResult;
import com.umarbhutta.xlightcompanion.okHttp.model.Rulecondition;
import com.umarbhutta.xlightcompanion.okHttp.model.Ruleresult;
import com.umarbhutta.xlightcompanion.okHttp.model.Rules;
import com.umarbhutta.xlightcompanion.okHttp.model.Schedule;
import com.umarbhutta.xlightcompanion.okHttp.requests.RequestAddRules;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Umar Bhutta.
 * 新的规则页面
 */
public class AddControlRuleActivity extends AppCompatActivity {

    private String TAG = AddControlRuleActivity.class.getSimpleName();
    private LinearLayout llBack;
    private TextView btnSure;
    private TextView tvTitle;
    private ImageButton ib_add_term, ib_add_result;
    private ListView lv_term, lv_control_actioncmd, lv_term_time,lv_control_actionnotify;

    private TextView tv_no_data1, tv_no_data2;

    private Rules rules;

    //执行条件
    private List<Rulecondition> mRuleconditionList;

    private Rulecondition mRulecondition;

    public static List<Schedule> mScheduleList = new ArrayList<Schedule>();
    public static List<Condition> mConditionList = new ArrayList<Condition>();

    public static List<String> mScheduleListStr = new ArrayList<String>();

    //执行结果
    private List<Ruleresult> mRuleresultList;

    private Ruleresult mRuleresult;

    public static List<Actioncmd> mActioncmdList = new ArrayList<Actioncmd>();
    public static List<Actionnotify> mActionnotify = new ArrayList<Actionnotify>();

    //    private TermAdapter termAdapter;
    private TermTopAdapter termTopAdapter;
    private TermBottomAdapter termBottomAdapternew;

    private ResultTopAdapter resultTopAdapter;
    private ResultBottomAdapter resultBottomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_control_rule);
        //hide nav bar
        getSupportActionBar().hide();

        rules = new Rules();

        //执行条件
        mRuleconditionList = new ArrayList<Rulecondition>();

        mRulecondition = new Rulecondition();

        mRulecondition.schedule = mScheduleList;
        mRulecondition.condition = mConditionList;

        mRuleconditionList.add(mRulecondition);
        rules.rulecondition = mRuleconditionList;

        //执行结果
        mRuleresultList = new ArrayList<Ruleresult>();

        mRuleresult = new Ruleresult();

        mRuleresult.actioncmd = mActioncmdList;
        mRuleresult.actionnotify = mActionnotify;

        mRuleresultList.add(mRuleresult);
        rules.ruleresult = mRuleresultList;

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (termBottomAdapternew != null) {
            termBottomAdapternew.notifyDataSetChanged();
        }
        if (termTopAdapter != null) {
            termTopAdapter.notifyDataSetChanged();
        }
        if (resultTopAdapter != null) {
            resultTopAdapter.notifyDataSetChanged();
        }
        if (resultBottomAdapter != null) {
            resultBottomAdapter.notifyDataSetChanged();
        }
        if(mScheduleList.size()>0 || mConditionList.size()>0){
            tv_no_data1.setVisibility(View.GONE);
        }
        if(mActioncmdList.size()>0 || mActionnotify.size()>0){
            tv_no_data2.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        llBack = (LinearLayout) findViewById(R.id.ll_back);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_term_time = (ListView) findViewById(R.id.lv_term_time);
        btnSure = (TextView) findViewById(R.id.tvEditSure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定提交按钮
                rules.rulename="rule1";
                rules.relationtype=1;
                rules.type=1;
                rules.status=1;
                rules.userId= UserUtils.getUserInfo(getApplicationContext()).getId();
                List<Rulecondition> rulecondition = new ArrayList<Rulecondition>();
                List<Ruleresult> ruleresult = new ArrayList<Ruleresult>();
                mRulecondition.schedule = mScheduleList;
                mRulecondition.condition = mConditionList;
                mRuleresult.actionnotify=mActionnotify;
                mRuleresult.actioncmd=mActioncmdList;
                rulecondition.add(mRulecondition);
                ruleresult.add(mRuleresult);
                rules.rulecondition=rulecondition;
                rules.ruleresult=ruleresult;
                RequestAddRules.getInstance().createRule(AddControlRuleActivity.this,rules,new RequestAddRules.OnCreateRuleCallback() {

                    @Override
                    public void mOnCreateRuleCallbackFail(int code, String errMsg) {
                        Logger.e(TAG,"errMsg=" + errMsg);
                        ToastUtil.showToast(AddControlRuleActivity.this, "errMsg=" + errMsg);//
                    }

                    @Override
                    public void mOnCreateRuleCallbackSuccess(CreateRuleResult mCreateRuleResult) {
                        Logger.e(TAG,"mCreateRuleResult=" + mCreateRuleResult.code);
                        ToastUtil.showToast(AddControlRuleActivity.this, "mCreateRuleResult=" + mCreateRuleResult.msg);//
//                        finish();
                    }
                });
            }
        });
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("新建规则");
        ib_add_term = (ImageButton) findViewById(R.id.ib_add_term);
        ib_add_result = (ImageButton) findViewById(R.id.ib_add_result);
        lv_term = (ListView) findViewById(R.id.lv_term);
        lv_control_actioncmd = (ListView) findViewById(R.id.lv_control_actioncmd);
        lv_control_actionnotify = (ListView) findViewById(R.id.lv_control_actionnotify);

        tv_no_data1 = (TextView) findViewById(R.id.tv_no_data1);
        tv_no_data2 = (TextView) findViewById(R.id.tv_no_data2);

        ib_add_term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加启动条件
                onFabPressed(EntryConditionActivity.class);
            }
        });
        ib_add_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 添加执行结果
                onFabPressed(ControlRuseltActivity.class);
            }
        });

//        termAdapter = new TermAdapter(getApplicationContext(), mRuleconditionList);
//        lv_term.setAdapter(termAdapter);
        //执行条件
        termTopAdapter = new TermTopAdapter(getApplicationContext(), mScheduleList);
        lv_term.setAdapter(termTopAdapter);
        termBottomAdapternew = new TermBottomAdapter(getApplicationContext(), mConditionList);
        lv_term_time.setAdapter(termBottomAdapternew);

        //执行结果
        resultTopAdapter = new ResultTopAdapter(getApplicationContext(), mActioncmdList);
        lv_control_actioncmd.setAdapter(resultTopAdapter);
        resultBottomAdapter = new ResultBottomAdapter(getApplicationContext(), mActionnotify);
        lv_control_actionnotify.setAdapter(resultBottomAdapter);
    }

    private void onFabPressed(Class activity) {
        Intent intent = new Intent(this, activity);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DEVICE_CONTROL", rules);
        intent.putExtra("BUNDLE", bundle);
        startActivityForResult(intent, 2018);
    }

    /**
     * 添加规则
     */
    private class TermAdapter extends BaseAdapter {

        private LayoutInflater inflater;//这个一定要懂它的用法及作用
        private Context mContext;
        private List<Rulecondition> mTermsList;

        private TermAdapter(Context context, List<Rulecondition> termsList) {
            this.mContext = context;
            this.mTermsList = termsList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mTermsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mTermsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_rulecondition, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.lv_top = (ListView) convertView.findViewById(R.id.lv_itemtop);
                holder.lv_bottom = (ListView) convertView.findViewById(R.id.lv_itembottom);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.lv_top.setAdapter(new TermTopAdapter(mContext, mTermsList.get(position).schedule));
            //根据innerlistview的高度机损parentlistview item的高度
            setListViewHeightBasedOnChildren(holder.lv_top);
            holder.lv_bottom.setAdapter(new TermBottomAdapter(mContext, mTermsList.get(position).condition));
            //根据innerlistview的高度机损parentlistview item的高度
            setListViewHeightBasedOnChildren(holder.lv_bottom);
            return convertView;
        }

        class ViewHolder {
            private ListView lv_top, lv_bottom;
        }
    }

    private class TermTopAdapter extends BaseAdapter {//时间

        private LayoutInflater inflater;//这个一定要懂它的用法及作用
        private Context mContext;
        private List<Schedule> mTopTermsList;

        private TermTopAdapter(Context context, List<Schedule> termsTopList) {
            this.mContext = context;
            this.mTopTermsList = termsTopList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mTopTermsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mTopTermsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_term, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.tvStr = (TextView) convertView.findViewById(R.id.tv_str);
                holder.imageView = (ImageButton) convertView.findViewById(R.id.ib_minus);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Schedule schedule = mTopTermsList.get(position);

            holder.tvStr.setText(schedule.hour + ":" + schedule.minute + "     " + mScheduleListStr.get(position));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mScheduleListStr.remove(position);
                    mTopTermsList.remove(schedule);
                    if(mScheduleList.size()>0 || mConditionList.size()>0){
                        tv_no_data1.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView tvStr;
            private ImageButton imageView;
        }
    }

    private class TermBottomAdapter extends BaseAdapter {

        private LayoutInflater inflater;//这个一定要懂它的用法及作用
        private Context mContext;
        private List<Condition> mTermsBottomList;

        private TermBottomAdapter(Context context, List<Condition> termsBottomList) {
            this.mContext = context;
            this.mTermsBottomList = termsBottomList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mTermsBottomList.size();
        }

        @Override
        public Object getItem(int position) {
            return mTermsBottomList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_term, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.tvStr = (TextView) convertView.findViewById(R.id.tv_str);
                holder.imageView = (ImageButton) convertView.findViewById(R.id.ib_minus);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Condition condition = mTermsBottomList.get(position);
            holder.tvStr.setText(condition.ruleconditionname + "   " + condition.operator + "   " + condition.rightValue);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTermsBottomList.remove(condition);
                    if(mScheduleList.size()>0 || mConditionList.size()>0){
                        tv_no_data1.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView tvStr;
            private ImageButton imageView;
        }
    }

    /**
     * 执行结果
     */
    private class ResultAdapter extends BaseAdapter {

        private LayoutInflater inflater;//这个一定要懂它的用法及作用
        private Context mContext;
        private List<Ruleresult> mResultsList;

        private ResultAdapter(Context context, List<Ruleresult> resultsList) {
            this.mContext = context;
            this.mResultsList = resultsList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mResultsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mResultsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

//                convertView = inflater.inflate(R.layout.item_result, null);
                convertView = inflater.inflate(R.layout.item_ruleresult, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.lv_top = (ListView) convertView.findViewById(R.id.lv_itemtop);
                holder.lv_bottom = (ListView) convertView.findViewById(R.id.lv_itembottom);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.lv_top.setAdapter(new ResultTopAdapter(mContext, mResultsList.get(position).actioncmd));
            //根据innerlistview的高度机损parentlistview item的高度
            setListViewHeightBasedOnChildren(holder.lv_top);
            holder.lv_bottom.setAdapter(new ResultBottomAdapter(mContext, mResultsList.get(position).actionnotify));
            //根据innerlistview的高度机损parentlistview item的高度
            setListViewHeightBasedOnChildren(holder.lv_bottom);
            return convertView;
        }

        class ViewHolder {
            private ListView lv_top, lv_bottom;
        }
    }

    private class ResultTopAdapter extends BaseAdapter {

        private LayoutInflater inflater;//这个一定要懂它的用法及作用
        private Context mContext;
        private List<Actioncmd> mResultsList;

        private ResultTopAdapter(Context context, List<Actioncmd> resultsList) {
            this.mContext = context;
            this.mResultsList = resultsList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mResultsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mResultsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_result, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.tvStr = (TextView) convertView.findViewById(R.id.tv_result);
                holder.imageView = (ImageButton) convertView.findViewById(R.id.ib_minus);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Actioncmd actioncmd = mResultsList.get(position);
            holder.tvStr.setText(actioncmd.actioncmdfield.get(0).cmd+" "+actioncmd.actioncmdfield.get(0).paralist.replace("{","").replace("}",""));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mResultsList.remove(actioncmd);
                    if(mActioncmdList.size()>0 || mActionnotify.size()>0){
                        tv_no_data2.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView tvStr;
            private ImageButton imageView;
        }
    }

    private class ResultBottomAdapter extends BaseAdapter {

        private LayoutInflater inflater;//这个一定要懂它的用法及作用
        private Context mContext;
        private List<Actionnotify> mResultsList;

        private ResultBottomAdapter(Context context, List<Actionnotify> resultsList) {
            this.mContext = context;
            this.mResultsList = resultsList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mResultsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mResultsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.item_result, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.tvStr = (TextView) convertView.findViewById(R.id.tv_result);
                holder.imageView = (ImageButton) convertView.findViewById(R.id.ib_minus);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Actionnotify actionnotify = mResultsList.get(position);
            if(StringUtil.isNotEmpty(actionnotify.msisdn,true)){
                holder.tvStr.setText(actionnotify.msisdn+" "+actionnotify.content);
            }else{
                holder.tvStr.setText(actionnotify.emailaddress+" "+actionnotify.content);
            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mResultsList.remove(actionnotify);
                    if(mActioncmdList.size()>0 || mActionnotify.size()>0){
                        tv_no_data2.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView tvStr;
            private ImageButton imageView;
        }
    }

    /**
     * @param listView 此方法是本次listview嵌套listview的核心方法：计算parentlistview item的高度。
     *                 如果不使用此方法，无论innerlistview有多少个item，则只会显示一个item。
     **/
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
