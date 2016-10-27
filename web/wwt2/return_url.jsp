<%@ page import="xd.fw.AliPayUtil" %>
<%@ page import="xd.fw.FwUtil" %>
<%@ page import="xd.dl.service.ParkService" %>
<%@ page import="xd.dl.bean.PayOrder" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="xd.dl.bean.ParkInfo" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    ParkService parkService = (ParkService)FwUtil.getBean("parkService");
    String out_trade_no = request.getParameter("out_trade_no");
    PayOrder order = parkService.get(PayOrder.class,out_trade_no);
    if (order == null){
        Logger.getLogger("return_url").error("no order for trade no:" + out_trade_no);
        return;
    }
    ParkInfo parkInfo = parkService.get(ParkInfo.class, order.getParkId());
    if (AliPayUtil.verify(request.getParameterMap(),parkInfo.getPartnerId())){
        request.getRequestDispatcher("aliReturn.cmd").forward(request, response);
    } else {
        out.println("验证出错！");
    }
%>