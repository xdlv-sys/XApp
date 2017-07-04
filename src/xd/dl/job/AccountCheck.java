package xd.dl.job;

public class  AccountCheck {
    
	 public String sStartTime;	//东汇开始结算时间

    public String sEndTime;	//东汇结束结算时间

     public int iOrderTotalNum;//订单总数

     public float fOrderTotalMny;//订单总金额
    
     public float fOfflineTotalMny;//线下支付总金额

     public float fOnlineTotalMny;//线上支付总金额

     public float fDisTotalMny;//平台优惠总金额
     
     public float fBusiTotalMny;//商户优惠总金额
 
     public int iReturn;//调用返回值
}