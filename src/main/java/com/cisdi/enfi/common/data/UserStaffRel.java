package com.cisdi.enfi.common.data;

import javax.persistence.*;

@javax.persistence.Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "rel_user_person")
public class UserStaffRel extends ExtItem{
    @Column(unique=false, nullable=true)
    public String getFk_person_id(){
        return (String)hashAttributes.get("fk_person_id");
    }

    public void setFk_person_id(String fk_person_id){
        hashAttributes.put("fk_person_id",fk_person_id);
    }

    @Column(unique=false, nullable=true)
    public String getFk_user_id(){
        return (String)hashAttributes.get("fk_user_id");
    }

    public void setFk_user_id(String fk_user_id){
        hashAttributes.put("fk_user_id",fk_user_id);
    }
}
