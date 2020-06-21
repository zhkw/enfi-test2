package com.cisdi.enfi.common.data;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
@Table(
        name = "ctp_rel_user_role"
)
public class User2Role extends Relation {
    private static final long serialVersionUID = 1L;

    public User2Role() {
    }
}
