package com.cisdi.enfi.common.data;

import org.hibernate.annotations.PolymorphismType;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
@Table(
        name = "ctp_resource"
)
@org.hibernate.annotations.Entity(
        polymorphism = PolymorphismType.EXPLICIT
)
public class Resource extends Root {
    private static final long serialVersionUID = 1L;

    public Resource() {
    }
}
