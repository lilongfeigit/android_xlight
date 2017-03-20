package com.umarbhutta.xlightcompanion.control;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.ToastUtil;
import com.umarbhutta.xlightcompanion.okHttp.model.Rows;
import com.umarbhutta.xlightcompanion.okHttp.model.SceneListResult;

/**
 * Created by Umar Bhutta.
 */
public class ScenarioSelectListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private SceneListResult mSceneListResult;

    public ScenarioSelectListAdapter(Context context, SceneListResult mSceneListResult) {
        this.mSceneListResult = mSceneListResult;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scenario_list_item, parent, false);
        return new ScenarioListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ScenarioListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return mSceneListResult.rows.size();
    }

    private class ScenarioListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView scenarioIndex, scenarioTitle, scenarioDescription;
        private ImageView scenarioDelete;

        private ImageView scenarioIcon;
        private TextView scenarioText;
        private int mPosition;
        private Rows infos;

        public ScenarioListViewHolder(View itemView) {
            super(itemView);
            scenarioIndex = (TextView) itemView.findViewById(R.id.scenarioIndex);
            scenarioTitle = (TextView) itemView.findViewById(R.id.scenarioTitle);
            scenarioDescription = (TextView) itemView.findViewById(R.id.scenarioDescription);
            scenarioDelete = (ImageView) itemView.findViewById(R.id.scenarioDelete);

            itemView.setOnClickListener(this);
            scenarioDelete.setOnClickListener(this);

            scenarioIcon = (ImageView) itemView.findViewById(R.id.icon_scenario);
            scenarioText = (TextView) itemView.findViewById(R.id.text_scenario);
        }

        public void bindView(int position) {

            mPosition = position;
            infos = mSceneListResult.rows.get(position);

            scenarioText.setText(infos.scenarioname);

        }

        @Override
        public void onClick(View v) {
            onFabPressed(v, infos);
        }

    }

    private void onFabPressed(View view, Rows infos) {
//        Intent intent = new Intent(mContext, AddScenarioNewActivity.class);
//        intent.putExtra("from", "list");
//        intent.putExtra("infos", infos);
//        mContext.startActivity(intent);
        ToastUtil.showToast(mContext,"选择了该场景");
    }

}
