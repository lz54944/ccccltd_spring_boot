package cn.ccccltd.beans;

import java.util.List;

import lombok.Data;

/**
 * 数节点，测试前台控件使用
 * 
 * @author ccccltd
 */
@Data
public class TreeNode {

	private long id;

	private String name;

	private String icon = "edit";

	private List<TreeNode> subnodes;

	public TreeNode(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

}
