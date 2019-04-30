package com.share_will.mobile;

import com.share_will.mobile.model.entity.AlarmEntity;
import com.share_will.mobile.model.entity.BatteryEntity;
import com.share_will.mobile.model.entity.BespeakEntity;
import com.share_will.mobile.model.entity.CabinetEntity;
import com.share_will.mobile.model.entity.ChargeBatteryEntity;
import com.share_will.mobile.model.entity.ChargeOrderEntity;
import com.share_will.mobile.model.entity.ChargeStakeEntity;
import com.share_will.mobile.model.entity.ChargingEntity;
import com.share_will.mobile.model.entity.CityEntity;
import com.share_will.mobile.model.entity.DepositRefundEntity;
import com.share_will.mobile.model.entity.NotifyMessageEntity;
import com.share_will.mobile.model.entity.PackageEntity;
import com.share_will.mobile.model.entity.PackageOrderEntity;
import com.share_will.mobile.model.entity.RecordEntity;
import com.share_will.mobile.model.entity.RescueEntity;
import com.share_will.mobile.model.entity.StationEntity;
import com.share_will.mobile.model.entity.UserInfo;
import com.ubock.library.base.BaseEntity;
import com.ubock.library.base.BaseRecyclerViewAdapter;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface ApiService {
    /**
     * 登录
     *
     * @param loginName 账号/手机号
     * @param password  密码
     * @param userType  用户类型，0普通用户，1管理员
     * @return
     */
    @FormUrlEncoded
    @POST("user/logon")
    Observable<BaseEntity<UserInfo>> login(@Field("loginName") String loginName,
                                           @Field("password") String password,
                                           @Field("userType") int userType);

    /**
     * 登录机柜后台
     *
     * @param loginName
     * @param cabinetId
     * @param userType
     * @return
     */
    @FormUrlEncoded
    @POST("user/logon")
    Observable<BaseEntity<Object>> loginCMS(@Field("loginName") String loginName,
                                            @Field("cabinetId") String cabinetId,
                                            @Field("userType") int userType);


    /**
     * 注册
     *
     * @param userId   账号/手机号
     * @param userName 姓名
     * @param password 密码
     * @param verCode  验证码
     * @param customer 渠道
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    Observable<BaseEntity<Object>> register(@Field("userId") String userId,
                                            @Field("userName") String userName,
                                            @Field("password") String password,
                                            @Field("verCode") String verCode,
                                            @Field("customer") String customer,
                                            @Field("stationId") String stationId);

    /**
     * 发送验证码
     *
     * @param userId 手机号
     * @return
     */
    @FormUrlEncoded
    @POST("user/verCode")
    Observable<BaseEntity<Object>> sendVerifyCode(@Field("userId") String userId);

    /**
     * 找回密码
     *
     * @param userId   手机号
     * @param password 密码
     * @param verCode  验证码
     * @return
     */
    @FormUrlEncoded
    @POST("user/updatePassword")
    Observable<BaseEntity<Object>> updatePassword(@Field("userId") String userId,
                                                  @Field("password") String password,
                                                  @Field("verCode") String verCode);

    /**
     * 获取机柜列表
     *
     * @param cityCode  城市区号
     * @param longitude
     * @param latitude
     * @return
     */
    @FormUrlEncoded
    @POST("cabinetMap/list")
    Observable<BaseEntity<List<CabinetEntity>>> getCabinetList(@Field("city") String cityCode,
                                                               @Field("longitude") double longitude,
                                                               @Field("latitude") double latitude);

    /**
     * 获取烟感预警信息
     *
     * @param
     * @param
     */
    @FormUrlEncoded
    @POST("user/smoke/alarm")
    Observable<BaseEntity<AlarmEntity>> getAlarmList(@Field("userId") String id,
                                                     @Field("token") String token);


    /**
     * 获取可换电池数量
     *
     * @param sn 机柜sn
     * @return
     */
    @FormUrlEncoded
    @POST("cabinetElectric/getFullBatteryCount")
    Observable<BaseEntity<Map<String, Integer>>> getFullBattery(@Field("sn") String sn);

    /**
     * 扫码换电
     *
     * @param userId    账号
     * @param cabinetId 机柜sn
     * @param time      时间截
     * @param appType   应用类型：0充电柜登录，1AndroidApp，2iosApp，3Android小程序，4ios小程序
     * @return
     */
    @FormUrlEncoded
    @POST("message/cabinetSave")
    Observable<BaseEntity<Object>> scanCodeLogin(@Field("userId") String userId,
                                                 @Field("cabinetId") String cabinetId,
                                                 @Field("time") String time,
                                                 @Field("appType") int appType);

    /**
     * 扫码充电
     *
     * @param appType 应用类型：0充电柜登录，1AndroidApp，2iosApp，3Android小程序，4ios小程序
     * @return
     */
    @FormUrlEncoded
    @POST("message/cabinetSave")
    Observable<BaseEntity<Object>> chargeScan(@Field("userId") String userId,
                                              @Field("token") String token,
                                              @Field("cabinetId") String cabinetId,
                                              @Field("time") String time,
                                              @Field("type") int type,
                                              @Field("appType") int appType);

    /**
     * 扫码结束充电
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("order/balance")
    Observable<BaseEntity<ChargeOrderEntity>> stopChargeScan(@Field("userId") String userId,
                                                             @Field("token") String token,
                                                             @Field("cabinetId") String cabinetId);

    /**
     * 充电电池信息
     *
     * @param userId
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("order/recharge/detail")
    Observable<BaseEntity<ChargeBatteryEntity>> getChargeBatteryInfo(@Field("userId") String userId,
                                                                     @Field("token") String token);


    /**
     * 获取用户账号余额、押金、套餐
     *
     * @param userId 账号
     * @return
     */
    @FormUrlEncoded
    @POST("pay/account")
    Observable<BaseEntity<UserInfo>> getBalance(@Field("userId") String userId);

    /**
     * 获取用户账号余额、押金、套餐
     *
     * @param userId 账号
     * @return
     */
    @FormUrlEncoded
    @POST("user/getBatteryCurrent")
    Observable<BaseEntity<Map<String, String>>> getBattery(@Field("userId") String userId);

    /**
     * 获取当前电池信息
     *
     * @param userId 账号
     * @return
     */
    @FormUrlEncoded
    @POST("user/getBatteryCurrent")
    Observable<BaseEntity<BatteryEntity>> getBatteryInfo(@Field("userId") String userId, @Field("token") String token);

    /**
     * 绑定电池
     *
     * @param userId 账号
     * @return
     */
    @FormUrlEncoded
    @POST("user/binding/battery")
    Observable<BaseEntity<Object>> bindBattery(@Field("userId") String userId, @Field("batteryId") String batteryId);


    /**
     * 关闭告警
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("user/smoke/confirm")
    Observable<BaseEntity<Object>> closeAlarm(@Field("userId") String userId,
                                              @Field("devtype") String devtype,
                                              @Field("deveui") String deveui);

    /**
     * 获取消费记录
     *
     * @param userId 账号
     * @return
     */
    @FormUrlEncoded
    @POST("pay/record")
    Observable<BaseEntity<List<RecordEntity>>> getConsumeList(@Field("userId") String userId,
                                                              @Field("pn") int page,
                                                              @Field("ps") int size);

    /**
     * 获取套餐列表
     *
     * @param userId 账号
     * @return
     */
    @FormUrlEncoded
    @POST("package/list")
    Observable<BaseEntity<List<PackageEntity>>> getPackageList(@Field("userId") String userId);

    /**
     * 生成套餐订单
     *
     * @param userId
     * @param packageId
     * @param activityId
     * @param money
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("order/createPackage")
    Observable<BaseEntity<PackageOrderEntity>> createPackageOrder(@Field("userId") String userId,
                                                                  @Field("packageId") long packageId,
                                                                  @Field("activityId") long activityId,
                                                                  @Field("money") int money,
                                                                  @Field("type") int type);


    /**
     * 支付套餐
     *
     * @param userId
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST("pay/package")
    Observable<BaseEntity<Object>> payPackageOrder(@Field("userId") String userId,
                                                   @Field("orderId") String orderId);

    /**
     * 支付
     *
     * @param userId
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST("pay/money")
    Observable<BaseEntity<Object>> payMoneyOrder(@Field("userId") String userId,
                                                 @Field("orderId") String orderId);

    /**
     * 生成充值订单
     *
     * @param userId
     * @param money
     * @return
     */
    @FormUrlEncoded
    @POST("order/createRecharge")
    Observable<BaseEntity<String>> crateRechargeOrder(@Field("userId") String userId,
                                                      @Field("money") int money);

    /**
     * 检测新版本
     *
     * @param versionName
     * @param versionCode
     * @param type        0为机柜，1为手机
     * @param customer    渠道：只手机有效，机柜为空
     * @param userId      用户手机号
     * @return
     */
    @FormUrlEncoded
    @POST("upgrade/checkVersion")
    Observable<BaseEntity<Map<String, String>>> checkVersion(@Field("versionName") String versionName,
                                                             @Field("versionCode") int versionCode,
                                                             @Field("type") int type,
                                                             @Field("customer") String customer,
                                                             @Field("userId") String userId);

    /**
     * 上传位置
     *
     * @param userId
     * @param longitude
     * @param latitude
     * @param range
     * @return
     */
    @FormUrlEncoded
    @POST("batteryMap/save")
    Observable<BaseEntity<Object>> uploadLocation(@Field("userId") String userId,
                                                  @Field("longitude") double longitude,
                                                  @Field("latitude") double latitude,
                                                  @Field("range") float range);

    /**
     * 获取充电选项:时间、价格
     *
     * @param sn 机柜SN
     * @return
     */
    @FormUrlEncoded
    @POST("chargeConfig/list")
    Observable<BaseEntity<List<ChargingEntity>>> getChargingList(@Field("sn") String sn);

    /**
     * 生成充电订单
     *
     * @param sn       机柜SN
     * @param userid   用户手机
     * @param channel  充电通道
     * @param chargeid 选项id
     * @return
     */
    @FormUrlEncoded
    @POST("chargeOrder/create")
    Observable<BaseEntity<Object>> createChargingOrder(@Field("sn") String sn,
                                                       @Field("userid") String userid,
                                                       @Field("channel") int channel,
                                                       @Field("chargeid") String chargeid);

    /**
     * 获取已开通城市列表
     *
     * @return
     */
    @GET("city/list")
    Observable<BaseEntity<List<CityEntity>>> getCityList();

    /**
     * 获取站点列表
     *
     * @param areaCode 城市区号
     * @return
     */
    @FormUrlEncoded
    @POST("user/station")
    Observable<BaseEntity<List<StationEntity>>> getStationList(@Field("areaCode") String areaCode,
                                                               @Field("type") int type);

    /**
     * 申请救援
     *
     * @param stationId   站点ID
     * @param userId      用户手机
     * @param rescueCause 救援原因
     * @return
     */
    @FormUrlEncoded
    @POST("battery/rescue/save")
    Observable<BaseEntity<Object>> applyRescue(@Field("stationId") long stationId,
                                               @Field("userId") String userId,
                                               @Field("rescueCause") String rescueCause);


    /**
     * 获取救援列表
     *
     * @param userId 用户手机
     * @return
     */
    @FormUrlEncoded
    @POST("battery/rescue/list")
    Observable<BaseEntity<List<RescueEntity>>> getRescueList(@Field("userId") String userId);

    /**
     * 取消救援
     *
     * @param userId 用户手机
     * @param id     救援记录ID
     * @return
     */
    @FormUrlEncoded
    @POST("battery/rescue/cancel")
    Observable<BaseEntity<Object>> cancelRescue(@Field("id") long id,
                                                @Field("userId") String userId);

    /**
     * 申请退押金
     *
     * @param userId       用户手机
     * @param bankUserName 银行卡人姓名
     * @param bankName     银行名称
     * @param bankCard     银行卡号
     * @param desc         退款备注
     * @return
     */
    @FormUrlEncoded
    @POST("refund/save")
    Observable<BaseEntity<Object>> applyRefund(@Field("userId") String userId,
                                               @Field("bankUserName") String bankUserName,
                                               @Field("bankName") String bankName,
                                               @Field("bankCard") String bankCard,
                                               @Field("desc") String desc);

    /**
     * 取消申请退押金
     *
     * @param id 退款id
     * @return
     */
    @FormUrlEncoded
    @POST("refund/cancel")
    Observable<BaseEntity<Object>> cancelApplyRefund(@Field("id") long id);

    /**
     * 申请退押金详情
     *
     * @param userId 用户id
     * @return
     */
    @FormUrlEncoded
    @POST("refund/detail")
    Observable<BaseEntity<DepositRefundEntity>> applyRefundDetail(@Field("userId") String userId);

    /**
     * 获取预约换电状态
     *
     * @param userId 账号
     * @return
     */
    @FormUrlEncoded
    @POST("battery/subscribe/get")
    Observable<BaseEntity<BespeakEntity.DataBean>> getBespeakInfo(@Field("userId") String userId);


    /**
     * 添加预约换电,提交预约
     *
     * @param userId
     * @param cabinetId 机柜SN
     * @return
     */
    @FormUrlEncoded
    @POST("battery/subscribe")
    Observable<BaseEntity<BespeakEntity.AddDataBean>> addBespeak(@Field("userId") String userId, @Field("cabinetId") String cabinetId);

    /**
     * 修改预约换电状态
     *
     * @param id
     * @param userId
     * @param status 0预约中1预约成功待取电2已取电3取消预约
     * @param desc
     * @return
     */
    @FormUrlEncoded
    @POST("battery/subscribe/cancel")
    Observable<BaseEntity<Object>> updateBespeak(@Field("id") String id,
                                                 @Field("userId") String userId,
                                                 @Field("status") String status,
                                                 @Field("desc") String desc);

    /**
     * 每日提醒信息
     *
     * @param userId
     * @return
     */
    @FormUrlEncoded
    @POST("notify/message")
    Observable<BaseEntity<NotifyMessageEntity>> getNotifyMessage(@Field("userId") String userId);

    /**
     * 获取支付宝预支付订单
     *
     * @param payType   支付类型，0：app支付， 1:扫码支付
     * @param appId     应用id
     * @param orderType 1包月套餐，0换电订单/充值
     * @param orderId   订单号
     * @param body      商品描述
     * @return
     */
    @FormUrlEncoded
    @POST("pay/getAliPayOrder")
    Observable<BaseEntity<String>> getAliPayOrder(@Field("payType") int payType,
                                                  @Field("appId") String appId,
                                                  @Field("orderType") int orderType,
                                                  @Field("orderId") String orderId,
                                                  @Field("body") String body);

    /**
     * 获取微信预支付订单
     *
     * @param appId     appId
     * @param orderType 1包月套餐，0换电订单/充值
     * @param orderId   订单号
     * @param body      商品描述
     * @return
     */
    @FormUrlEncoded
    @POST("pay/getWeiXinOrder")
    Observable<BaseEntity<Map<String, String>>> getWeiXinOrder(@Field("appId") String appId, @Field("orderType") int orderType,
                                                               @Field("orderId") String orderId,
                                                               @Field("body") String body);

    /**
     * 通过站长手机号获取站点
     *
     * @param phone 站长手机号
     * @return
     */
    @FormUrlEncoded
    @POST("user/station/master")
    Observable<BaseEntity<List<StationEntity>>> getStationForPhone(@Field("userId") String phone);


    /**
     * 扫码领取电池
     *
     * @param cabinetId 机柜SN
     * @param userId    用户手机
     * @return
     */
    @FormUrlEncoded
    @POST("user/battery/get")
    Observable<BaseEntity<Object>> scanCodeGetBattery(@Field("cabinetId") String cabinetId,
                                                      @Field("userId") String userId);

    /**
     * 异常扫码领取电池
     *
     * @param cabinetId 机柜SN
     * @param userId    用户手机
     * @return
     */
    @FormUrlEncoded
    @POST("user/battery/fail")
    Observable<BaseEntity<Object>> exceptionScanCodeGetBattery(@Field("cabinetId") String cabinetId,
                                                               @Field("userId") String userId);

    /**
     * 获取用户充电信息
     * @return
     */
    @POST("cabinet/stake/user")
    Observable<BaseEntity<ChargeStakeEntity>> getChargingInfo();

    /**
     * 获取充电桩状态
     *
     * @param cabinetId 机柜SN
     * @return
     */
    @FormUrlEncoded
    @POST("cabinet/stake/list")
    Observable<BaseEntity<List<ChargeStakeEntity>>> getStakeStatus(@Field("cabinetId") String cabinetId);

    /**
     * 充电桩充电或结束充电结果
     *
     * @param cabinetId 机柜SN
     * @param userId    用户手机
     * @param index    充电桩号
     * @param status    设置的状态，0关(断电)，1开(通电)
     * @return
     */
    @FormUrlEncoded
    @POST("cabinet/stake/save")
    Observable<BaseEntity<Object>> stakeCharging(@Field("cabinetId") String cabinetId,
                                                 @Field("userId") String userId,
                                                 @Field("index") int index,
                                                 @Field("status") int status);

}
