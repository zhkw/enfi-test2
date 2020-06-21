package com.cisdi.enfi.common.data;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "pbs_view_staff_deptrel")
public class StaffDeptRel extends ExtItem{
    @Column(unique=false, nullable=true)
    public String getStaffId(){
        return (String)hashAttributes.get("staffId");
    }

    public void setStaffId(String staffId){
        hashAttributes.put("staffId",staffId);
    }

    @Column(unique=false, nullable=true)
    public String getDepartmentId(){
        return (String)hashAttributes.get("departmentId");
    }

    public void setDepartmentId(String departmentId){
        hashAttributes.put("departmentId",departmentId);
    }

    @Column(unique=false, nullable=true)
    public String getPrimaryFlag(){
        return (String)hashAttributes.get("primaryFlag");
    }

    public void setPrimaryFlag(String primaryFlag){
        hashAttributes.put("primaryFlag",primaryFlag);
    }
}
