package com.cisdi.enfi.common.data;

import java.util.List;
import java.util.Map;

public class EnvironmentVariable {
    private User user;// 当前登录User对象
    private String userId;// 用户ID
    private String userName;// 用户名称
    private String staffId;// 员工ID
    private String staffName;// 员工名称
    private List<String> participatedProject;// 用户参与的项目列表
    private Map<String, Object> projectRole;// 用户在项目中的角色
    private List<String> departmentIds;// 员工部门Id
    private Map<String, Object> majors;// 员工专业Id
    private String roleId;// 用户对应角色Id
    private String roleName;// 用户角色名称

    private String projectId;
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 当前登录用户ID
     *
     * @return
     */
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 当前登录用户名称
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 当前登录员工ID
     *
     * @return
     */
    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    /**
     * 当前登录员工名称
     *
     * @return
     */
    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * 当前用户参与的项目列表
     *
     * @return
     */
    public List<String> getParticipatedProject() {
        return participatedProject;
    }

    public void setParticipatedProject(List<String> participatedProject) {
        this.participatedProject = participatedProject;
    }

    /**
     * 当前用户在项目中的角色
     *
     * @return
     */
    public Map<String, Object> getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(Map<String, Object> projectRole) {
        this.projectRole = projectRole;
    }

    public List<String> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<String> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public Map<String, Object> getMajors() {
        return majors;
    }

    public void setMajors(Map<String, Object> majors) {
        this.majors = majors;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
