package com.changshagaosu.roadtools.bean;

import com.changshagaosu.roadtools.ui.view.tree.TreeNodeGroup;
import com.changshagaosu.roadtools.ui.view.tree.TreeNodeId;
import com.changshagaosu.roadtools.ui.view.tree.TreeNodePid;

/**
 * @Annotation <p>描述</p>
 * @Auth Sunny
 * @date 2017/9/8
 * @Version V1.0.0
 */

public class UserBean {

    // 每个列表项的数据对象的成员变量，都要有以下三个注解
    @TreeNodeId
    private int id = 0;
    @TreeNodePid
    private int parentId = 0;
    @TreeNodeGroup
    private boolean isGroup = false;

    private String name = null;

    public UserBean(int id, int parentId, boolean isGroup, String name) {
        this.id = id;
        this.parentId = parentId;
        this.isGroup = isGroup;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
