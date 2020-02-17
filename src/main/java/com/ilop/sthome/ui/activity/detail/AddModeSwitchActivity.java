package com.ilop.sthome.ui.activity.detail;

import android.support.v7.widget.LinearLayoutManager;

import com.example.common.base.BaseActivity;
import com.example.common.utils.LiveDataBus;
import com.ilop.sthome.data.event.EventAnswerOK;
import com.ilop.sthome.data.greenDao.AutomationBean;
import com.ilop.sthome.data.greenDao.DeviceInfoBean;
import com.ilop.sthome.data.greenDao.SceneBean;
import com.ilop.sthome.data.greenDao.SceneSwitchBean;
import com.ilop.sthome.network.api.SendCommandAli;
import com.ilop.sthome.network.api.SendSceneGroupDataAli;
import com.ilop.sthome.ui.adapter.detail.SceneSwitchAdapter;
import com.ilop.sthome.ui.dialog.BaseListDialog;
import com.ilop.sthome.utils.CoderALiUtils;
import com.ilop.sthome.utils.greenDao.AutomationDaoUtil;
import com.ilop.sthome.utils.greenDao.DeviceDaoUtil;
import com.ilop.sthome.utils.greenDao.SceneDaoUtil;
import com.ilop.sthome.utils.greenDao.SceneSwitchDaoUtil;
import com.ilop.sthome.utils.tools.ByteUtil;
import com.siterwell.familywellplus.R;
import com.siterwell.familywellplus.databinding.ActivityModeSwitchBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * @author skygge
 * @date 2019-12-11.
 * GitHub：javofxu@github.com
 * email：skygge@yeah.net
 * description：添加自定义场景切换
 */
public class AddModeSwitchActivity extends BaseActivity<ActivityModeSwitchBinding>{

    private String mDeviceName;
    private int mDeviceId;
    private DeviceInfoBean mDevice;

    private SceneSwitchAdapter mAdapter;
    private SceneSwitchBean mSwitch;
    private SendSceneGroupDataAli mSend;
    private List<SceneBean> mScene;
    private List<String> mSysModelName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mode_switch;
    }

    @Override
    protected void initialize() {
        super.initialize();
        immersionStatusBar(true);
        EventBus.getDefault().register(this);
        mDeviceName = getIntent().getStringExtra("deviceName");
        mDeviceId = getIntent().getIntExtra("deviceId", -1);
        mDevice = DeviceDaoUtil.getInstance().findByDeviceId(mDeviceName, mDeviceId);
        DeviceInfoBean deviceInfoBean = DeviceDaoUtil.getInstance().findGatewayByDeviceName(mDevice.getDeviceName());
        mSend = new SendSceneGroupDataAli(this, deviceInfoBean);
    }

    @Override
    protected void initView() {
        super.initView();
        mScene = SceneDaoUtil.getInstance().findAllScene(mDeviceName);
        mSysModelName = SceneDaoUtil.getInstance().findAllSceneName(mDeviceName);
        mAdapter = new SceneSwitchAdapter(mContext, mDevice);
        mDBind.modeList.setLayoutManager(new LinearLayoutManager(mContext));
        mDBind.modeList.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        if(mSysModelName.size()>=3){
            mSysModelName.set(0,getResources().getString(R.string.home_mode));
            mSysModelName.set(1,getResources().getString(R.string.out_mode));
            mSysModelName.set(2,getResources().getString(R.string.sleep_mode));
        }
        mAdapter.setList(mScene);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mDBind.ivModeBack.setOnClickListener(view -> finish());
        LiveDataBus.get().with("Switch_Mode", Integer.class).observe(this, integer -> {
            BaseListDialog mDialog = new BaseListDialog(mContext, i -> {
                mSwitch = new SceneSwitchBean();
                mSwitch.setDes_sid(mScene.get(i).getSid());
                mSwitch.setSrc_sid(mScene.get(integer).getSid());
                mSwitch.setDelay(0);
                mSwitch.setDeviceName(mDevice.getDeviceName());
                mSwitch.setDeviceId(mDevice.getDevice_ID());
                String code = getCode(mScene.get(integer),mSwitch.getDeviceId(),mSwitch.getDes_sid());
                String crc = ByteUtil.CRCmakerCharAndCode(code);
                mSend.increaceSceneGroup(code+crc);
            });
            String[] name = new String[mScene.size()];
            name = mSysModelName.toArray(name);
            mDialog.setMsgAndButton(name, getString(R.string.cancel));
            mDialog.setTitle(getString(R.string.please_choose_mode));
            mDialog.show();
        });
    }

    @Subscribe          //订阅事件FirstEvent
    public  void onEventMainThread(EventAnswerOK event) {
        if (event.getData_str1().length() == 9) {
            int cmd = Integer.parseInt(event.getData_str1().substring(0, 4), 16);
            if (cmd == SendCommandAli.INCREACE_SCENE_GROUP) {
                if ("OK".equals(event.getData_str2())) {
                    SceneSwitchDaoUtil.getInstance().insertSwitch(mSwitch);
                    mAdapter.notifyDataSetChanged();
                    showToast(getString(R.string.success_set));
                }
            }
        }
    }

    private String getCode(SceneBean mScene, int thiseqid, int des_sid) {

        String name = null;
        if(mScene.getSid()>2){
            name = mScene.getModleName();
        }else{
            switch (mScene.getSid()){
                case 0:
                    name = "Home";
                    break;
                case 1:
                    name = "Away";
                    break;
                case 2:
                    name = "Sleep";
                    break;
                default:break;
            }
        }

        int  length = 0;
        length+=2;//the total num length

        String id2 = String.valueOf(mScene.getSid());
        length += 1;//the scene id

        String btnNum = "";
        List<SceneSwitchBean> mSwitch = SceneSwitchDaoUtil.getInstance().findSwitchBySid(mScene.getSid(), mDevice.getDeviceName());



        String shortcut = "";

        boolean flag_this_eqid = false;
        for (int i = 0;i<mSwitch.size();i++){
            String eqid = "";
            String dessid = "";
            if(thiseqid == mSwitch.get(i).getDeviceId()){
                flag_this_eqid = true;

                if (Integer.toHexString(thiseqid).length()<2){  //new mid
                    eqid = "000"+Integer.toHexString(thiseqid);
                }else{
                    eqid ="00"+Integer.toHexString(thiseqid);
                }

                if (Integer.toHexString(des_sid).length()<2){  //new mid
                    dessid = "0"+Integer.toHexString(des_sid)+"000000";
                }else{
                    dessid =Integer.toHexString(des_sid)+"000000";
                }

                shortcut+=(eqid+dessid+"00");
                length += 7;

            }else{

                if (Integer.toHexString(mSwitch.get(i).getDeviceId()).length()<2){  //new mid
                    eqid = "000"+Integer.toHexString(mSwitch.get(i).getDeviceId());
                }else{
                    eqid ="00"+Integer.toHexString(mSwitch.get(i).getDeviceId());
                }

                if (Integer.toHexString(mSwitch.get(i).getDes_sid()).length()<2){  //new mid
                    dessid = "0"+Integer.toHexString(mSwitch.get(i).getDes_sid())+"000000";
                }else{
                    dessid =Integer.toHexString(mSwitch.get(i).getDes_sid())+"000000";
                }

                shortcut+=(eqid+dessid+"00");
                length += 7;
            }



        }

        if(!flag_this_eqid){
            String eqid = "";
            String dessid = "";
            if (Integer.toHexString(thiseqid).length()<2){  //new mid
                eqid = "000"+Integer.toHexString(thiseqid);
            }else{
                eqid ="00"+Integer.toHexString(thiseqid);
            }

            if (Integer.toHexString(des_sid).length()<2){  //new mid
                dessid = "0"+Integer.toHexString(des_sid)+"000000";
            }else{
                dessid =Integer.toHexString(des_sid)+"000000";
            }

            shortcut+=(eqid+dessid+"00");
            length += 7;


            if (Integer.toHexString(mSwitch.size()+1).length()<2){  //new mid
                btnNum = "0"+Integer.toHexString(mSwitch.size()+1);
            }else{
                btnNum =Integer.toHexString(mSwitch.size()+1);
            }
            length+=1;//button num

        }else{
            int ds = mSwitch.size();

            if (Integer.toHexString(ds).length()<2){  //new mid
                btnNum = "0"+Integer.toHexString(ds);
            }else{
                btnNum =Integer.toHexString(ds);
            }
            length+=1;//button num
        }

        //self-define scene num
        length+=1;
        int scene =0;
        //scene id
        String sceneCode ="";
        List<AutomationBean> autoList = AutomationDaoUtil.getInstance().findAllAutoBySid(mScene.getSid(), mDevice.getDeviceName());
        if(autoList.size()>0){
            for(int i = 0; i<autoList.size();i++){
                scene++;
                length++;
                String singleCode;
                //do count
//                if (Integer.toHexString(sceneLists.get(i).getActivityId()).length()<2){  defore
                if (Integer.toHexString(autoList.get(i).getMid()).length()<2){  //new mid
                    singleCode = "0"+Integer.toHexString(autoList.get(i).getMid());
                }else{
                    singleCode =Integer.toHexString(autoList.get(i).getMid());
                }
                sceneCode += singleCode;
            }
        }else {
            length+=0;
            sceneCode = "";
        }

        //增加了颜色的定制
        length+=1;

        String oooo ="0";
        if(Integer.toHexString(length+32).length() < 4){
            for (int i = 0 ; i<4-Integer.toHexString(length+32).length()-1;i++ ){
                oooo += 0;
            }
            oooo += Integer.toHexString(length+32);
        }else{
            oooo = Integer.toHexString(length+32);
        }

        String oo = "0";
        if(Integer.toHexString(scene).length()<2){
            oo = oo + Integer.toHexString(scene);
        }else{
            oo = Integer.toHexString(scene);
        }
        String ds = CoderALiUtils.getAscii(name);
        String fullCode = oooo +"0"+id2 + ds + btnNum  + oo + shortcut + sceneCode + mScene.getColor();
        return fullCode;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
