package com.cisdi.enfi.common.data;

import org.hibernate.annotations.PolymorphismType;

import javax.persistence.*;

@javax.persistence.Entity
@Table(
        name = "ctp_item"
)
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
@org.hibernate.annotations.Entity(
        polymorphism = PolymorphismType.EXPLICIT
)
public class Item extends Resource {
    private static final long serialVersionUID = 1L;

    public Item() {
    }

    @Column(
            unique = false,
            nullable = true
    )
    public String getBpmLocked() {
        return (String)this.hashAttributes.get("bpmLocked");
    }

    public void setBpmLocked(String bpmLocked) {
        this.hashAttributes.put("bpmLocked", bpmLocked);
    }

    @Column(
            unique = false,
            nullable = true
    )
    public String getCreater() {
        return (String)this.hashAttributes.get("creater");
    }

    public void setCreater(String creater) {
        this.hashAttributes.put("creater", creater);
    }

    @Column(
            unique = false,
            nullable = true
    )
    public Boolean getNotEditAble() {
        return (Boolean)this.hashAttributes.get("notEditAble");
    }

    public void setNotEditAble(Boolean editAble) {
        this.hashAttributes.put("notEditAble", editAble);
    }
}

