package edu.sdust.insapp.bean;

import java.util.List;

public class TreeNode {
    private String id;// 树节点ID
    private String value;// 树节点value
    private String treeInsideId;// 树节点ID
    private String label;// 树节点名称
    private List<TreeNode> children;// 子节点
    private String parent;// 父节点ID
    private boolean isLeaf;// 是否为叶子节点
    private boolean isRoot;//是否为根节点

    public TreeNode(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public TreeNode(String id, String label, List<TreeNode> children) {
        this.id = id;
        this.label = label;
        this.children = children;
    }

    public TreeNode(String id, String value, String treeInsideId, String label, List<TreeNode> children, String parent, boolean isLeaf, boolean isRoot) {
        this.id = id;
        this.value = value;
        this.treeInsideId = treeInsideId;
        this.label = label;
        this.children = children;
        this.parent = parent;
        this.isLeaf = isLeaf;
        this.isRoot = isRoot;
    }

    @Override
    public String toString() {
        return ""+id+": "+label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTreeInsideId() {
        return treeInsideId;
    }

    public void setTreeInsideId(String treeInsideId) {
        this.treeInsideId = treeInsideId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }
}
