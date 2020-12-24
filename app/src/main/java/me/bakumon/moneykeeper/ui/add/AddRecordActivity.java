/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.bakumon.moneykeeper.ui.add;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.MainType;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.databinding.ActivityAddRecordBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.DateUtils;
import me.bakumon.moneykeeper.utill.SoftInputUtils;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * HomeActivity
 *
 */
public class AddRecordActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = AddRecordActivity.class.getSimpleName();
    private static final String TAG_PICKER_DIALOG = "Datepickerdialog";

    private ActivityAddRecordBinding mBinding;

    private AddRecordViewModel mViewModel;

    private Date mCurrentChooseDate = DateUtils.getTodayDate();
    private Calendar mCurrentChooseCalendar = Calendar.getInstance();
    private int mCurrentType;

    private RecordWithType mRecord;

    private OptionsPickerView pvAccount, pvMember, pvType, pvTransfer,pvTransfer_to, pvStore, pvProject;

    //account
    private ArrayList<FatherBean> accountOptions1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> accountOptions2Items = new ArrayList<>();
    private int account_choose_id;

    //account in transfer
    private ArrayList<FatherBean> transferOptions1Items = new ArrayList<>();
    private ArrayList<FatherBean> transferOptions2Items = new ArrayList<>();
    private int account_choose_from, account_choose_to;

    //member
    private ArrayList<FatherBean> memberOptions1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> memberOptions2Items = new ArrayList<>();
    private int member_choose_id;

    //type
    private ArrayList<FatherBean> typeOptions1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> typeOptions2Items = new ArrayList<>();
    //    private int type_choose_father_id, type_choose_son_id;
    private int type_choose_father_id;

    //Store
    private ArrayList<FatherBean> storeOptions1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> storeOptions2Items = new ArrayList<>();
    private int store_choose_id;

    //Project
    private ArrayList<FatherBean> projectOptions1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> projectOptions2Items = new ArrayList<>();
    private int project_choose_id;

    private ArrayList<String> options2Items_01 = new ArrayList<>();
    private ArrayList<String> options2Items_02 = new ArrayList<>();
    private ArrayList<String> options2Items_03 = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddRecordViewModel.class);
//      在initView中获得了跳转到修改页面的数据mRecord
        initView();

        initData();

        //显示pickerView
        //wsl:account
        mBinding.sortAccount.setOnClickListener(this);
        //wsl:member
        mBinding.sortMember.setOnClickListener(this);
        //wsl:分类
        mBinding.sortType.setOnClickListener(this);
        //wsl:transfer account
        mBinding.sortTransferAccount.setOnClickListener(this);
        mBinding.sortTransferAccountTo.setOnClickListener(this);
        mBinding.sortShop.setOnClickListener(this);
        mBinding.sortProject.setOnClickListener(this);


    }

    private void initData() {
        getAllMainTypes();
        getAllAccounts();
        getAllMembers();
        getAllStores();
        getAllProjects();
    }

    private void initView() {
//        这个就是点击修改会跳转过来的地方
        mRecord = (RecordWithType) getIntent().getSerializableExtra(Router.ExtraKey.KEY_RECORD_BEAN);

        mBinding.titleBar.ibtClose.setBackgroundResource(R.drawable.ic_back);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());

        mBinding.edtRemark.setOnEditorActionListener((v, actionId, event) -> {
            SoftInputUtils.hideSoftInput(mBinding.typePageOutlay);
            mBinding.keyboard.setEditTextFocus();
            return false;
        });

        if (mRecord == null) {
            mCurrentType = RecordType.TYPE_OUTLAY;
            mBinding.titleBar.setTitle(getString(R.string.text_add_record));
            mBinding.edtTypeFather.setText("请选择分类");


        } else {
//            当mRecord不是空的时候，显示的数据
            mCurrentType = mRecord.mRecordTypes.get(0).type;
            mBinding.titleBar.setTitle(getString(R.string.text_modify_record));
            mBinding.edtRemark.setText(mRecord.remark);
            mBinding.keyboard.setText(BigDecimalUtil.fen2Yuan(mRecord.money));
            mCurrentChooseDate = mRecord.time;
            mCurrentChooseCalendar.setTime(mCurrentChooseDate);
            mBinding.qmTvDate.setText(DateUtils.getWordTime(mCurrentChooseDate));

//            不行，会闪退
//            mBinding.edtTypeFather.setText(typeOptions2Items.get(0).get(mRecord.mainTypeId));
//            mBinding.edtAccount.setText(mRecord.accountId);
//            mBinding.edtAccountFrom.setText(mRecord.accountId); //id有问题
//            mBinding.edtAccountTo.setText(mRecord.accountId);   //id有问题
//            mBinding.edtMember.setText(mRecord.memberId);
//            mBinding.edtShop.setText(mRecord.storeId);
//            mBinding.edtProject.setText(mRecord.projectId);

        }

        mBinding.keyboard.setAffirmClickListener(text -> {
            if (mRecord == null) {
                insertRecord(text);
            } else {
                modifyRecord(text);
            }
        });

        //时间
        mBinding.qmTvDate.setOnClickListener(v -> {
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    (view, year, monthOfYear, dayOfMonth) -> {
                        mCurrentChooseDate = DateUtils.getDate(year, monthOfYear + 1, dayOfMonth);
                        mCurrentChooseCalendar.setTime(mCurrentChooseDate);
                        mBinding.qmTvDate.setText(DateUtils.getWordTime(mCurrentChooseDate));
                    }, mCurrentChooseCalendar);
            dpd.setMaxDate(Calendar.getInstance());
            dpd.show(getFragmentManager(), TAG_PICKER_DIALOG);
        });
        //wsl:transfer
        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rb_outlay) {
                mCurrentType = RecordType.TYPE_OUTLAY;
                mBinding.typePageOutlay.setVisibility(View.VISIBLE);
                mBinding.typePageIncome.setVisibility(View.GONE);
                mBinding.typePageTransfer.setVisibility(View.GONE);
                mBinding.sortAccount.setVisibility(View.VISIBLE);
                mBinding.sortTransferAccount.setVisibility(View.GONE);
                mBinding.sortTransferAccountTo.setVisibility(View.GONE);
                initTypePicker(0);
                initAccountPicker();

            } else if (checkedId == R.id.rb_income){
                //收入
                mCurrentType = RecordType.TYPE_INCOME;
                mBinding.typePageOutlay.setVisibility(View.GONE);
                mBinding.typePageIncome.setVisibility(View.VISIBLE);
                mBinding.typePageTransfer.setVisibility(View.GONE);
                mBinding.sortAccount.setVisibility(View.VISIBLE);
                mBinding.sortTransferAccount.setVisibility(View.GONE);
                mBinding.sortTransferAccountTo.setVisibility(View.GONE);
                initTypePicker(1);
                initAccountPicker();
            } else {
                //转账
                mCurrentType = RecordType.TYPE_TRANSFER;
                mBinding.typePageOutlay.setVisibility(View.GONE);
                mBinding.typePageIncome.setVisibility(View.GONE);
                mBinding.typePageTransfer.setVisibility(View.VISIBLE);
                mBinding.sortAccount.setVisibility(View.GONE);
                mBinding.sortTransferAccount.setVisibility(View.VISIBLE);
                mBinding.sortTransferAccountTo.setVisibility(View.VISIBLE);
                initTypePicker(2);
                initTransferPicker();
            }

        });

        //wsl：传递页面类型给xml页面
        mBinding.setCurPageType(mCurrentType);

    }

    private void insertRecord(String text) {
        // 防止重复提交
        mBinding.keyboard.setAffirmEnable(false);
        Record record = new Record();
        Record record_trans = new Record(); //转账单独来了一个
        record.money = BigDecimalUtil.yuan2FenBD(text);
        record_trans.money = BigDecimalUtil.yuan2FenBD(text);
        record.remark = mBinding.edtRemark.getText().toString().trim();
        record_trans.remark = mBinding.edtRemark.getText().toString().trim();
        record.time = mCurrentChooseDate;
        record_trans.time = mCurrentChooseDate;
        record.createTime = new Date();
        record_trans.createTime = new Date();
        //      wsl:transfer
        if (mCurrentType == RecordType.TYPE_OUTLAY) {
            record.recordTypeId = mBinding.typePageOutlay.getCurrentItem().id;
        } else if (mCurrentType == RecordType.TYPE_INCOME) {
            record.recordTypeId = mBinding.typePageIncome.getCurrentItem().id;
        } else {
            //暂时-------------------------------------------------------------------------------------------
            record.recordTypeId = mBinding.typePageTransfer.getCurrentItem().id;
            record_trans.recordTypeId = mBinding.typePageTransfer.getCurrentItem().id;
//            record.recordTypeId = 11;
//            record_trans.recordTypeId =11;
//            System.out.println("id:"+record_trans.recordTypeId);
        }


        //wsl account pickerView
        //-----------------------------------------------------------------------------------------------有问题
        record_trans.accountId = account_choose_id + 1;

        //wsl member pickerView
        record.memberId =member_choose_id + 1;
        record_trans.memberId =member_choose_id + 1;

        //wsl store
        record.storeId = store_choose_id + 1;
        //wsl project
        record.projectId = project_choose_id + 1;

        //新加8————————————————————————————————————————————
        record.mainTypeId = type_choose_father_id+1;
        record_trans.mainTypeId = type_choose_father_id+1;
        if (mCurrentType == RecordType.TYPE_OUTLAY){
            record.type = 0;
            record.accountId = account_choose_id + 1;
        } else if (mCurrentType == RecordType.TYPE_INCOME){
            record.type = 1;
            record.accountId = account_choose_id + 1;
        } else {
            record.type = 2;
            record_trans.type = 3;
            record.accountId = account_choose_from + 1;
            record_trans.accountId = account_choose_to + 1;
        }


        mDisposable.add(mViewModel.insertRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish,
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（新增记录失败的时候）", throwable);
                                finish();
                            } else {
                                Log.e(TAG, "新增记录失败", throwable);
                                mBinding.keyboard.setAffirmEnable(true);
                                ToastUtils.show(R.string.toast_add_record_fail);
                            }
                        }
                ));
        if (mCurrentType == RecordType.TYPE_TRANSFER){
            mDisposable.add(mViewModel.insertRecord(record_trans)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::finish,
                            throwable -> {
                                if (throwable instanceof BackupFailException) {
                                    ToastUtils.show(throwable.getMessage());
                                    Log.e(TAG, "备份失败（新增记录失败的时候）", throwable);
                                    finish();
                                } else {
                                    Log.e(TAG, "新增记录失败", throwable);
                                    mBinding.keyboard.setAffirmEnable(true);
                                    ToastUtils.show(R.string.toast_add_record_fail);
                                }
                            }
                    ));
        }
    }

    private void modifyRecord(String text) {
        // 防止重复提交
        mBinding.keyboard.setAffirmEnable(false);
        mRecord.money = BigDecimalUtil.yuan2FenBD(text);
        mRecord.remark = mBinding.edtRemark.getText().toString().trim();
        mRecord.time = mCurrentChooseDate;

        //      wsl:transfer
        if (mCurrentType == RecordType.TYPE_OUTLAY) {
            mRecord.recordTypeId = mBinding.typePageOutlay.getCurrentItem().id;
        } else if (mCurrentType == RecordType.TYPE_INCOME) {
            mRecord.recordTypeId = mBinding.typePageIncome.getCurrentItem().id;
        } else {
            mRecord.recordTypeId = mBinding.typePageTransfer.getCurrentItem().id;
        }

        //wsl:account pickerView
        mRecord.accountId = account_choose_id;
        //wsl:member pickerView
        mRecord.memberId = member_choose_id + 1;
        //wsl:store pickerView
        mRecord.storeId = store_choose_id;

        mRecord.projectId = project_choose_id;

//        wsl:不知是否会出错？
        if (mCurrentType == RecordType.TYPE_OUTLAY) {
            mRecord.type = 0;
        } else if (mCurrentType == RecordType.TYPE_INCOME){
            mRecord.type = 1;
        } else {
            mRecord.type = 2;
        }

        mDisposable.add(mViewModel.updateRecord(mRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish,
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（记录修改失败的时候）", throwable);
                                finish();
                            } else {
                                Log.e(TAG, "记录修改失败", throwable);
                                mBinding.keyboard.setAffirmEnable(true);
                                ToastUtils.show(R.string.toast_modify_record_fail);
                            }
                        }
                ));
    }

    //新加8---------------------------------------------------------------------------
    private void getAllMainTypes() {
        typeOptions1Items.add(new FatherBean(0, "一级分类"));
        mDisposable.add(mViewModel.getAllMainTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((mainTypes) -> {
                    ArrayList<String> options2Items_01 = new ArrayList<>();
                    for(int i=0;i<mainTypes.size();i++){
                        if (mainTypes.get(i).type == 0) {
//                            支出
                            this.options2Items_01.add(mainTypes.get(i).name);
                        } else if (mainTypes.get(i).type == 1) {
//                            收入
                            this.options2Items_02.add(mainTypes.get(i).name);
                        } else {
//                            转账
                            this.options2Items_03.add(mainTypes.get(i).name);
                        }

                    }
//控制台打印-------------------------------------------------------------------------------
                    System.out.println("输出获得的主类：");
                    for (String object1 : options2Items_01) {
                        System.out.println("1：");
                        System.out.println(object1);
                    }
                    for (String object2 : options2Items_02) {
                        System.out.println("2：");
                        System.out.println(object2);
                    }
                    for (String object3 : options2Items_03) {
                        System.out.println("3：");
                        System.out.println(object3);
                    }
                    getAllRecordTypes(-1);
                    initTypePicker(0);

                }, throwable -> {
                    ToastUtils.show(R.string.toast_get_types_fail);
                    Log.e(TAG, "获取成员数据失败", throwable);
                }));
    }

    //        获得二级分类
    private void getAllRecordTypes(int i) {
        mDisposable.add(mViewModel.getAllRecordTypesWithMain(i)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> {
                    mBinding.typePageOutlay.setNewData(recordTypes, RecordType.TYPE_OUTLAY);
                    mBinding.typePageIncome.setNewData(recordTypes, RecordType.TYPE_INCOME);
                    mBinding.typePageTransfer.setNewData(recordTypes, RecordType.TYPE_TRANSFER);

                    if (mCurrentType == RecordType.TYPE_OUTLAY) {
                        //支出
                        mBinding.typeChoice.rgType.check(R.id.rb_outlay);
                        mBinding.typePageOutlay.initCheckItem(mRecord);
                    } else if(mCurrentType == RecordType.TYPE_INCOME){
                        //收入
                        mBinding.typeChoice.rgType.check(R.id.rb_income);
                        mBinding.typePageIncome.initCheckItem(mRecord);
                    } else {
                        //转账
                        mBinding.typeChoice.rgType.check(R.id.rb_transfer);
                        mBinding.typePageTransfer.initCheckItem(mRecord);
                    }

                }, throwable -> {
                    ToastUtils.show(R.string.toast_get_types_fail);
                    Log.e(TAG, "获取类型数据失败", throwable);
                }));
    }

    //  将initxxxxPicerView()放在这里面就成功
    private void getAllAccounts(){
        accountOptions1Items.add(new FatherBean(0, "账户"));
        mDisposable.add(mViewModel.getAllAccounts()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((accounts) -> {
                                    ArrayList<String> options2Items_01 = new ArrayList<>();
                                    for(int i=0;i<accounts.size();i++){
                                        options2Items_01.add(accounts.get(i).name);
                                        transferOptions1Items.add(new FatherBean(i,accounts.get(i).name));
                                        transferOptions2Items.add(new FatherBean(i,accounts.get(i).name));
                                    }
//
                                    accountOptions2Items.add(options2Items_01);
//                            transfer页面

//                                initAccountPicker();
//                                initTransferPicker();
                                }, throwable -> {
                                    ToastUtils.show(R.string.toast_get_types_fail);
                                    Log.e(TAG, "获取账户数据失败", throwable);}
                        )
        );

    }

    private void getAllMembers() {
        memberOptions1Items.add(new FatherBean(0, "成员"));
        mDisposable.add(mViewModel.getAllMembers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((members) -> {
                    ArrayList<String> options2Items_01 = new ArrayList<>();
                    for(int i=0;i<members.size();i++){
                        options2Items_01.add(members.get(i).name);
                    }
                    memberOptions2Items.add(options2Items_01);
                    initMemberPicker();

                }, throwable -> {
                    ToastUtils.show(R.string.toast_get_types_fail);
                    Log.e(TAG, "获取成员数据失败", throwable);
                }));
    }

    private void getAllStores() {
        storeOptions1Items.add(new FatherBean(0, "商家"));
        mDisposable.add(mViewModel.getAllStores()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((stores) -> {
                    ArrayList<String> options2Items_01 = new ArrayList<>();
                    for(int i=0;i<stores.size();i++){
                        options2Items_01.add(stores.get(i).name);
                    }
                    storeOptions2Items.add(options2Items_01);
                    initStorePicker();

                }, throwable -> {
                    ToastUtils.show(R.string.toast_get_types_fail);
                    Log.e(TAG, "获取商家数据失败", throwable);
                }));
    }

    private void getAllProjects() {
        projectOptions1Items.add(new FatherBean(0, "项目"));
        mDisposable.add(mViewModel.getAllProjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((projects) -> {
                    ArrayList<String> options2Items_01 = new ArrayList<>();
                    for(int i=0;i<projects.size();i++){
                        options2Items_01.add(projects.get(i).name);
                    }
                    projectOptions2Items.add(options2Items_01);
                    initProjectPicker();

                }, throwable -> {
                    ToastUtils.show(R.string.toast_get_types_fail);
                    Log.e(TAG, "获取项目数据失败", throwable);
                }));
    }

    //  wsl:init PickerView
    private void initAccountPicker() {
//        /**
//         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
//         * 注意：数组下标从1开始
//         */

        pvAccount = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                //getPickerViewText这个函数需要自己在options1Items类中定义
                //在button得到了选择的东西，可以用在记一笔上。显示到textView里面
                String tx = accountOptions2Items.get(options1).get(options2);
                account_choose_id = options2;

                /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/
                mBinding.edtAccount.setText(tx);
            }
        })
                //这里是设置样式的
                .setTitleText("选择账户")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.GRAY)
                .setCancelColor(Color.RED)
                .setSubmitColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
//                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
//                    @Override
//                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
//                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(AddRecordActivity.this, str, Toast.LENGTH_SHORT).show();
//                    }
//                })
                .build();

//        pvOptions.setSelectOptions(1,1);
//        pvOptions.setPicker(options1Items);                //一级选择器*/
        pvAccount.setPicker(accountOptions1Items,accountOptions2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/


    }

    private void initMemberPicker() {
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         * 注意：数组下标从1开始
         */

        pvMember = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                //getPickerViewText这个函数需要自己在options1Items类中定义
                //在button得到了选择的东西，可以用在记一笔上。显示到textView里面
                String tx = memberOptions2Items.get(options1).get(options2);
                member_choose_id = options2;

                /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/
                mBinding.edtMember.setText(tx);
            }
        })
                //这里是设置样式的
                .setTitleText("选择成员")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                //wsl:改!
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.DKGRAY)
                .setCancelColor(Color.RED)
                .setSubmitColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();

//        pvOptions.setSelectOptions(1,1);
//        pvOptions.setPicker(options1Items);                //一级选择器*/
        pvMember.setPicker(memberOptions1Items,memberOptions2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/


    }

//    i = 0:out_lay
//    i = 1:income
//    i = 2:transfer

    private void initTypePicker(int i) {
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         * 注意：数组下标从1开始
         */
        pvType = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String tx;
                tx = typeOptions2Items.get(options1).get(options2);
                type_choose_father_id = options2;

                if(i==0){
                    getAllRecordTypes(options2+1);
                }
                else if(i==1){
                    getAllRecordTypes(options2Items_01.size()+options2+1);
                }
                else if(i==2){
//                    getAllRecordTypes(options2Items_01.size()+options2Items_02.size()+options2+1);
                }

                /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/
                // son 的名字 可能不能成功
                // 能显示字但是有问题
                mBinding.edtTypeFather.setText(tx);

            }
        })
                //这里是设置样式的
                .setTitleText("选择一级分类")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.GRAY)
                .setCancelColor(Color.RED)
                .setSubmitColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();
//      这个在上面的执行顺序之前
        if(i==0){
            typeOptions2Items.clear();
            typeOptions2Items.add(this.options2Items_01);
        }
        else if(i==1) {
            typeOptions2Items.clear();
            typeOptions2Items.add(this.options2Items_02);

        }
        else if(i==2){
            typeOptions2Items.clear();
            typeOptions2Items.add(this.options2Items_03);
        }
        for (ArrayList<String> object2 : typeOptions2Items) {
            for (String object3: object2){
                System.out.println(object3 );
            }
        }
        System.out.println("添加表~1" );
        pvType.setPicker(typeOptions1Items,typeOptions2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/


    }

    private void initTransferPicker() {
        //transfer的闪退我知道了，应该要有两级get
        pvTransfer = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String txFather = transferOptions1Items.get(options1).getPickerViewText();
                account_choose_from = options1;
                mBinding.edtAccountFrom.setText(txFather);
            }
        })
                .setTitleText("选择账户")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.GRAY)
                .setCancelColor(Color.RED)
                .setSubmitColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();


        pvTransfer_to = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String txFather = transferOptions2Items.get(options1).getPickerViewText();
                account_choose_to = options1;
                System.out.println("to2:"+account_choose_to);
                mBinding.edtAccountTo.setText(txFather);
            }
        })
                //这里是设置样式的
                .setTitleText("选择账户")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                //wsl:改!
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.GRAY)
                .setCancelColor(Color.RED)
                .setSubmitColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();

        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/
        typeOptions2Items.add(this.options2Items_03);
        pvTransfer.setPicker(transferOptions1Items);
        pvTransfer_to.setPicker(transferOptions2Items);
        pvType.setPicker(typeOptions1Items,typeOptions2Items);//二级选择器

    }

    private void initStorePicker() {
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         * 注意：数组下标从1开始
         */
        pvStore = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                //getPickerViewText这个函数需要自己在options1Items类中定义
                //在button得到了选择的东西，可以用在记一笔上。显示到textView里面
                String tx = storeOptions2Items.get(options1).get(options2);
                store_choose_id = options2;

                /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/
                mBinding.edtShop.setText(tx);
            }
        })
                //这里是设置样式的
                .setTitleText("选择商家")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                //wsl:改!
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.DKGRAY)
                .setCancelColor(Color.RED)
                .setSubmitColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();

//        pvOptions.setSelectOptions(1,1);
//        pvOptions.setPicker(options1Items);                //一级选择器*/
        pvStore.setPicker(storeOptions1Items,storeOptions2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/


    }

    private void initProjectPicker() {
        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         * 注意：数组下标从1开始
         */
        pvProject = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                //getPickerViewText这个函数需要自己在options1Items类中定义
                //在button得到了选择的东西，可以用在记一笔上。显示到textView里面
                String tx = projectOptions2Items.get(options1).get(options2);
                project_choose_id = options2;

                /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/
                mBinding.edtProject.setText(tx);
            }
        })
                //这里是设置样式的
                .setTitleText("选择项目")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                //wsl:改!
                .setSelectOptions(0, 0)//默认选中项
                .setBgColor(Color.WHITE)
                .setTitleBgColor(Color.WHITE)
                .setTitleColor(Color.DKGRAY)
                .setCancelColor(Color.RED)
                .setSubmitColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();

//        pvOptions.setSelectOptions(1,1);
//        pvOptions.setPicker(options1Items);                //一级选择器*/
        pvProject.setPicker(projectOptions1Items,projectOptions2Items);//二级选择器
        /*pvOptions.setPicker(options1Items, options2Items,options3Items);//三级选择器*/


    }

    @Override
    public void onClick(View view) {
        if (view == mBinding.sortAccount && pvAccount != null) {
            pvAccount.show(); //弹出条件选择器
        } else if(view == mBinding.sortMember && pvMember != null) {
            pvMember.show();
        } else if(view == mBinding.sortType && pvType != null) {
            pvType.show();
        } else if(view == mBinding.sortTransferAccount && pvTransfer != null) {
            pvTransfer.show();
        } else if(view == mBinding.sortTransferAccountTo  && pvTransfer_to != null){
            pvTransfer_to.show();
        } else if(view == mBinding.sortShop  && pvStore != null){
            pvStore.show();
        } else if(view == mBinding.sortProject  && pvProject != null){
            pvProject.show();
        }
    }
}
