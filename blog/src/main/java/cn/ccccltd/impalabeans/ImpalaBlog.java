package cn.ccccltd.impalabeans;

import cn.ccccltd.beans.BaseEntity;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Entity
@lombok.Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "cms_blog")
public class ImpalaBlog extends BaseEntity {

    private long id;

    private String title;

    private String content;

    private String type;

    private String createUserId;

}
