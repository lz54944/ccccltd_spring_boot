package cn.ccccltd.jpa;

import cn.ccccltd.beans.BaseEntity;
import cn.ccccltd.jms.JMSTool;
import cn.ccccltd.jms.JMSType;
import lombok.Setter;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;


public class JPAListener {

    @Setter
    static JMSTool jmsTool;

    @PostPersist
    public void createObject(BaseEntity o) {
        jmsTool.sendMessage(JMSType.CREATE, o);
    }

    @PostRemove
    public void removeObject(BaseEntity o) {
        jmsTool.sendMessage(JMSType.DELETE, o);
    }
}
