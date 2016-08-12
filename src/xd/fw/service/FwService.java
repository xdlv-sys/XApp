package xd.fw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xd.fw.bean.Mod;
import xd.fw.bean.Role;
import xd.fw.bean.User;

import java.util.List;

public interface FwService extends BaseService{

    User userLogin(User user) throws Exception;

    void saveOrUpdateUser(User user) throws Exception;

    void saveOrUpdateRole(Role role);
}
