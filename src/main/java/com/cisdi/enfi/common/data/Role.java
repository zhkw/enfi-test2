package com.cisdi.enfi.common.data;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
@Table(
        name = "ctp_role"
)
public class Role extends Item {
    private static final long serialVersionUID = 1L;

    public Role() {
    }

    @Column(
            unique = false,
            nullable = true
    )
    public String getDescription() {
        return (String)this.hashAttributes.get("description");
    }

    public void setDescription(String description) {
        this.hashAttributes.put("description", description);
    }

    @Column(
            unique = true,
            nullable = true
    )
    public String getName() {
        return (String)this.hashAttributes.get("name");
    }

    public void setName(String name) {
        this.hashAttributes.put("name", name);
    }
}

