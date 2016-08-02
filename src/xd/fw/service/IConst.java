package xd.fw.service;


public interface IConst {
    int INVALIDATE_INT = -1000;

    int OK = 200;

    int DEFAULT_REFERRER = 3;

    Byte VIP = 1, NON_VIP = 0;
    // user level: 0 非会员，1 会员，2黄金，3白金，4钻石
    byte UL_NON = 0, UL_NORMAL = 1, UL_GOLD = 2, UL_WHITE = 3, UL_DIAMOND = 4;
    //area level: 0 无效，5区代，6市代，7省代
    Byte AL_NON = 0, AL_ARE = 5, AL_CITY = 6, AL_PROVINCE = 7;

    // 交易状态 2已付款，4退货成功，5 己收货，6己分销
    Byte TR_PAY = 2, TR_REJECT = 4, TR_RECEIVED = 5, TR_COMPLETE = 6;

    //交易类型: 0 消费 1 提现
    Byte TR_TYPE_CONSUME = 0, TR_TYPE_MONEY = 1;
    //event status
    Byte EV_INI = 0, EV_PROCESSING = 1, EV_DONE = 2, EV_FAIL = 3,
    /*4: 消费额为负*/
    EV_FAIL_TF_NEGATIVE = 4, EV_FAIL_BALANCE_NEGATIVE = 5;

    //event type 1 用户属性更新 2 新交易 11 用户升级（等级，VIP) 12 用户余额发生变化
    Byte EV_USER_UPDATE = 1, EV_ADD_ORDER = 2,
    /*sub event*/EV_USER_UPGRADE = 11, EV_USER_SETTLEMENT = 12;
}
