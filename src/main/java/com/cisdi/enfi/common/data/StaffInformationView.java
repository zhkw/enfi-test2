package com.cisdi.enfi.common.data;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "pbs_view_StaffInformation")
public class StaffInformationView extends ExtItem {

    @Column(unique = false, nullable = true)
    public String getName() {
        return (String) hashAttributes.get("name");
    }

    public void setName(String name) {
        hashAttributes.put("name", name);
    }
}
