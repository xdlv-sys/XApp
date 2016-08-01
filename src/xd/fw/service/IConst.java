package xd.fw.service;


public interface IConst {
    int INVALIDATE_INT = -1000;

    int OK = 200;

    int DEFAULT_REFERRER = 3;

    Byte VIP = 1, NON_VIP = 0;
    // user level: 0 非会员，1 会员，2黄金，3白金，4钻石
    byte UL_NON = 0, UL_NORMAL = 1, UL_GOLD = 2, UL_WHITE = 3, UL_DIAMOND = 4;
    //area level: 0 无效，5区代，6市代，7省代
    Byte AL_NON = 0,AL_ARE = 5, AL_CITY = 6, AL_PROVINCE = 7;

    // 交易状态 2已付款，4退货成功，5 己收货，6己分销
    Byte TR_PAY = 2, TR_REJECT = 4, TR_RECEIVED = 5, TR_COMPLETE = 6;

    //event status
    Byte EV_INI = 0, EV_PROCESSING = 1, EV_DONE = 2, EV_FAIL = 3;

    //event type 1 用户新增和修改 2 新交易 11 用户升级需要通知mall
    Byte EV_USER_UPDATE = 1, EV_ADD_ORDER = 2,
            /*sub event*/EV_USER_UPGRADE = 11;
}
