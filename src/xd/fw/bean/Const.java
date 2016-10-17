package xd.fw.bean;


public interface Const {
    short PARK_PROXY_STATUS_DISCONNECT = 0, PARK_PROXY_STATUS_NORMAL = 1;

    short STATUS_INITIAL = 0, STATUS_SUCCESS = 1, STATUS_FAIL = 2;

    short ORDER_STATUS_REFUND_DONE = 7, ORDER_STATUS_REFUND_FAIL = 8;

    short PAY_WX = 0, PAY_ALI = 1;

    String SUCCESS_FLAG = "SUCCESS";
}
