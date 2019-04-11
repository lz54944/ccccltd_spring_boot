package cn.ccccltd.impalabeans;


import cn.ccccltd.beans.BaseEntity;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@lombok.Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "project_information")
public class ProjectInformation extends BaseEntity{

    private long id;

    private long orgId;

    private String name;

    private String shortName;

    private String manager;

    private String managerMobile;

    private String overview;

    private String constructStatusKey;

    private String constructStatus;

    private String constructTypeKey;

    private String constructType;

    private String constructPurposeKey;

    private String constructPurpose;

    private String structureTypeKey;

    private String structureType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date contractStart;

    @Temporal(TemporalType.TIMESTAMP)
    private Date contractFinish;

    @Temporal(TemporalType.TIMESTAMP)
    private Date planFinish;

    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStart;

    @Temporal(TemporalType.TIMESTAMP)
    private Date actualFinish;

    private double contractAmount;

    private String address;

    private String quantityDesc;

    private String biddingCompany;

    private String ownerCompany;

    private String designCompany;

    private String constructCompany;

    private String superviserCompany;

    private double longitude;

    private double latitude;

    private String originalId;

    private String originalSystemKey;

    private char isRemoved;

    private long version;

    private long tenantId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

}
