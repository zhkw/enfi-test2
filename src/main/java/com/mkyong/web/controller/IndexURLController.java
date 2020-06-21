package com.mkyong.web.controller;

import com.cisdi.enfi.common.controller.BaseController;
import com.cisdi.enfi.common.data.*;
import com.cisdi.enfi.common.exception.AbortMessageException;
import com.cisdi.enfi.common.service.CommonService;
import com.cisdi.enfi.common.utils.GeneralBeanOrMapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class IndexURLController extends BaseController {

    @Resource
    CommonService commonService;

    @RequestMapping("")
    public ModelAndView helloWorld(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        ModelAndView mv;
        EnvironmentVariable env = (EnvironmentVariable) request.getSession().getAttribute("EV");
//        if (env == null) {
//            mv = createSingleView("home/login");
//        } else {
//            mv = createLayoutView("project/project");
//        }

        mv = createSingleView("home/login");
        return mv;
    }

    @RequestMapping("/login")
    @ResponseBody
    public boolean login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam("username") String username,
            @RequestParam("password") String password) throws Exception {
        Boolean flag = false;
        if(!username.contains("admin") && !username.contains("system")){
//			int rs = AdTest.connect(PropHolderUtils.getSysConfig("ad.url"),PropHolderUtils.getSysConfig("ad.port"), username.toUpperCase(), password);
//			if(rs != 0){
//				return flag;
//			}
        }
        List<Root> userList = commonService.getObjListByCondition(
                User.class.getSimpleName(),
                " UPPER(obj.name)='"+username.toUpperCase()+"'");
        if (userList.size() == 0) {
            return flag;
        }
        Map<String, Object> user = userList.get(0).getHashAttributes();
        setSessionUser(request,user);
        flag = true;
        return flag;
    }

    private void setSessionUser(HttpServletRequest request, Map<String, Object> user) throws Exception{
        User userObj = GeneralBeanOrMapUtils.convertMap2Bean(User.class, user);
        String userId = user.get("id").toString();
        List<Root> user2roleList = commonService.getObjListByCondition(
                User2Role.class.getSimpleName(),
                "obj.leftId='"+userId+"'");
        if (user2roleList.size() == 0) {
            throw new AbortMessageException("没有找到该用户对应的角色!");
        }
        Map<String, Object> user2role = user2roleList.get(0).getHashAttributes();
        String roleId = user2role.get("rightId").toString();
        Root role = commonService.getObjById(Role.class.getSimpleName(), roleId);
        if (role == null) {
            throw new AbortMessageException("没有找到该用户对应的角色!");
        }
        String staffName = "", staffId = "";
        List<String> deptIds = new ArrayList<String>();
        List<Root> user2staffList = commonService.getObjListByCondition(
                UserStaffRel.class.getSimpleName(),
                "obj.fk_user_id ='"+userId+"'");
        if (user2staffList.size() != 0) {
            Map<String, Object> user2staff = user2staffList.get(0).getHashAttributes();
            staffId = user2staff.get("fk_person_id").toString();

            Root staffR = commonService.getObjById(StaffInformationView.class.getSimpleName(), staffId);
            if (staffR != null) {
                staffName = (String) staffR.getAttribute("name");
            }

            List<Root> staff2DeptList = commonService.getObjListByCondition(
                    StaffDeptRel.class.getSimpleName(),
                    "obj.staffId ='"+userId+"'");
            if (staff2DeptList.size()!=0) {
                for (Root staff2Dept : staff2DeptList) {
                    deptIds.add((String) staff2Dept.getAttribute("departmentId"));
                }
            }
        }
        EnvironmentVariable env = new EnvironmentVariable();
        env.setUser(userObj);
        env.setUserId(user.get("id").toString());
        env.setUserName(user.get("name").toString());
        env.setRoleId(roleId);
        env.setRoleName(role.getAttribute("name").toString());
        env.setStaffId(staffId);
        env.setStaffName(staffName);
        env.setDepartmentIds(deptIds);
        request.getSession().setAttribute("EV", env);
    }

    @RequestMapping("/logout")
    public ModelAndView logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) throws Exception {
        ModelAndView mv;
        request.getSession().removeAttribute("EV");;
        mv = createSingleView("home/login");
        return mv;
    }

}
