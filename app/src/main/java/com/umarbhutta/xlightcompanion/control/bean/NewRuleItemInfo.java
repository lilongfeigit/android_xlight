package com.umarbhutta.xlightcompanion.control.bean;

import android.text.TextUtils;

import com.umarbhutta.xlightcompanion.R;
import com.umarbhutta.xlightcompanion.Tools.StringUtil;
import com.umarbhutta.xlightcompanion.okHttp.model.Actioncmd;
import com.umarbhutta.xlightcompanion.okHttp.model.Actionnotify;
import com.umarbhutta.xlightcompanion.okHttp.model.Condition;
import com.umarbhutta.xlightcompanion.okHttp.model.Schedule;

/**
 * Created by luomengxin on 2017/4/23.
 * 规则添加执行条件和结果
 */

public class NewRuleItemInfo {
    //条件
    private Schedule mSchedule;
    private Condition mCondition;

    //结果
    private Actioncmd mActioncmd;
    private Actionnotify mActionnotify;

    private ControlRuleDevice mControlRuleDevice;//控制的结果

    /**
     * 用于显示添加规则页面的item
     */
    public String showText;

    public String getShowText() {
        return showText;
    }

    private void setShowText(String showText) {
        this.showText = showText;
    }

    public Schedule getmSchedule() {
        return mSchedule;
    }

    public void setmSchedule(Schedule mSchedule) {
        this.mSchedule = mSchedule;
        setShowText(mSchedule.hour + ":" + mSchedule.minute + " "+mSchedule.scheduleName);
    }

    public Condition getmCondition() {
        return mCondition;
    }

    public void setmCondition(Condition condition) {
        this.mCondition = condition;
        if(mCondition.conditionType==5 ||  mCondition.conditionType==6 || mCondition.conditionType==7
                || mCondition.conditionType==2 || mCondition.conditionType==3){
            setShowText(mCondition.ruleconditionname);
        }else if(mCondition.conditionType==8){
            setShowText(mCondition.attribute + "   "+mCondition.ruleconditionname + "   " + mCondition.temAbove + "   " + mCondition.rightValue);
        }else{
            setShowText(mCondition.ruleconditionname + "   " + mCondition.operator + "   " + mCondition.rightValue);
        }
    }

    public Actioncmd getmActioncmd() {
        return mActioncmd;
    }

    public void setmActioncmd(Actioncmd mActioncmd) {
        this.mActioncmd = mActioncmd;
        if(mActioncmd.actioncmdType==1){
            setShowText(mActioncmd.actioncmdfield.get(0).paralist.replace("{", "").replace("}", ""));
        }else {
            setShowText(mActioncmd.actioncmdfield.get(0).cmd + " " + mActioncmd.actioncmdfield.get(0).paralist.replace("{", "").replace("}", ""));
        }
    }

    public Actionnotify getmActionnotify() {
        return mActionnotify;
    }

    public void setmActionnotify(Actionnotify mActionnotify) {
        this.mActionnotify = mActionnotify;

        if(mActionnotify.actionnotifyType == 1){//邮箱通知
            setShowText(mActionnotify.name+" "+mActionnotify.emailaddress + " "+mActionnotify.subject + " " + mActionnotify.content);
        }else if(mActionnotify.actionnotifyType == 2){
            setShowText(mActionnotify.name+" "+mActionnotify.content);
        }else {
            if (StringUtil.isNotEmpty(mActionnotify.msisdn, true)) {
                setShowText(mActionnotify.msisdn + " " + mActionnotify.content);
            } else {
                if (TextUtils.isEmpty(mActionnotify.emailaddress)) {

                    setShowText(mActionnotify.content);
                } else {

                    setShowText(mActionnotify.emailaddress + " " + mActionnotify.content);
                }
            }
        }
    }

    public ControlRuleDevice getmControlRuleDevice() {
        return mControlRuleDevice;
    }

    public void setmControlRuleDevice(ControlRuleDevice mControlRuleDevice) {
        this.mControlRuleDevice = mControlRuleDevice;
        setShowText(mControlRuleDevice.roomName + " " + mControlRuleDevice.statues+" 亮度："+mControlRuleDevice.brightness+" 色温："+mControlRuleDevice.cct);
    }

    /**
     * 获取类型
     *
     * @return 0 Schedule，1 Condition， 2 Actioncmd， 3 Actionnotify
     */
    public int getType() {
        if (null != mSchedule) {
            return 0;
        }

        if (null != mCondition) {
            return 1;
        }

        if (null != mActioncmd) {
            return 2;
        }
        return 3;
    }

    @Override
    public String toString() {
        return "NewRuleItemInfo{" +
                "mSchedule=" + mSchedule +
                ", mCondition=" + mCondition +
                ", mActioncmd=" + mActioncmd +
                ", mActionnotify=" + mActionnotify +
                ", showText='" + showText + '\'' +
                '}';
    }
}
