package xd.fw.job;

import xd.fw.bean.JknEvent;
import xd.fw.service.IConst;

import java.util.Map;

interface UserHandler extends IConst {
    byte process(JknEvent event, Map<Integer, UserDesc> userMap);

}