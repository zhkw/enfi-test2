package com.cisdi.enfi.common.data;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "ceis_extItem")
public class ExtItem extends Item {
    @Column(unique = false, nullable = true)
    public String getUpdater() {
        return (String) hashAttributes.get("updater");
    }

    public void setUpdater(String updater) {
        hashAttributes.put("updater", updater);
    }
}
