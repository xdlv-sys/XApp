package xd.fw.action;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import xd.fw.bean.Mod;
import xd.fw.bean.Role;
import xd.fw.service.FwService;

import java.util.List;

@Controller
public class ModAction extends BaseAction{
    final static int ADMIN_ID = -10;
    @Autowired
    FwService fwService;
    List<Mod> mods;

    Role role;

    Mod mod;
    public String obtainUserMods() throws Exception {
        String filter = obtainFilterValue();
        int userId = ADMIN_ID;
        if (StringUtils.isNotBlank(filter)){
            userId = Integer.parseInt(filter);
        }
        mods = fwService.getUserMods(userId);
        return SUCCESS;
    }

    public String obtainModsByRole() throws Exception{
        mods = fwService.getModsByRole(role.getId());
        return SUCCESS;
    }

    public String delMod() throws Exception{
        if (mods != null){
            fwService.deleteMods(mods);
        }
        return FINISH;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String saveMod() throws Exception{
        fwService.saveOrUpdateMod(mod);
        return FINISH;
    }

    public void setMod(Mod mod) {
        this.mod = mod;
    }

    public Mod getMod() {
        return mod;
    }

    public List<Mod> getMods() {
        return mods;
    }

    public void setMods(List<Mod> mods) {
        this.mods = mods;
    }
}
