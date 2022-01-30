package com.nace.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "NACE_DETAILS")
@NoArgsConstructor
@AllArgsConstructor
public class NaceDetailsEntity implements Serializable {

    private static final long serialVersionUID = -2674412344139139754L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ORDERS")
    private Long order;

    @Column(name = "LEVEL")
    private Long level;

    @Column(name = "CODE")
    private String code;

    @Column(name = "PARENT")
    private String parent;

    @Column(name = "DESCRIPTION")
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ITEM_INCLUDES")
    private String itemIncludes;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ITEM_ALSO_INCLUDES")
    private String itemAlsoIncludes;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "RULINGS")
    private String rulings;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "ITEM_EXCLUDES")
    private String itemExcludes;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "REFERENCES_ISIC")
    private String referencesIsic;

    @Override
    public String toString() {
        return "NaceExcelDetails [id=" + id + ", order=" + order + ", level=" + level + ", code=" + code + ", parent="
                + parent + ", description=" + description + ", itemIncludes=" + itemIncludes + ", itemAlsoIncludes="
                + itemAlsoIncludes + ", rulings=" + rulings + ", itemExcludes=" + itemExcludes + ", referencesIsic="
                + referencesIsic + "]";
    }
}
