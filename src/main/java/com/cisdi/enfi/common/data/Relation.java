package com.cisdi.enfi.common.data;

import javax.persistence.*;

@javax.persistence.Entity
@Table(
        name = "ctp_relation"
)
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
public class Relation extends Root {
    private static final long serialVersionUID = 1L;

    public Relation() {
    }

    @Column(
            nullable = false
    )
    public String getLeftClassId() {
        return (String)this.hashAttributes.get("leftClassId");
    }

    public void setLeftClassId(String id) {
        this.hashAttributes.put("leftClassId", id);
    }

    @Column(
            nullable = false
    )
    public String getLeftId() {
        return (String)this.hashAttributes.get("leftId");
    }

    public void setLeftId(String id) {
        this.hashAttributes.put("leftId", id);
    }

    @Column(
            nullable = false
    )
    public String getRightClassId() {
        return (String)this.hashAttributes.get("rightClassId");
    }

    public void setRightClassId(String id) {
        this.hashAttributes.put("rightClassId", id);
    }

    @Column(
            nullable = false
    )
    public String getRightId() {
        return (String)this.hashAttributes.get("rightId");
    }

    public void setRightId(String id) {
        this.hashAttributes.put("rightId", id);
    }

    @Column(
            nullable = true
    )
    public Boolean getNotDeleteRel() {
        return (Boolean)this.hashAttributes.get("notDeleteRel");
    }

    public void setNotDeleteRel(Boolean notDeleteRel) {
        this.hashAttributes.put("notDeleteRel", notDeleteRel);
    }
}

