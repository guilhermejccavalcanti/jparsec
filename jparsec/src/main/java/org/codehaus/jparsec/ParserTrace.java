package org.codehaus.jparsec;

interface ParserTrace {

  static final ParserTrace DISABLED = new ParserTrace() {
    @Override public void push(String name) {}
    @Override public void pop() {}
    @Override public TreeNode getParentNode() { return null; }
    @Override public TreeNode getCurrentNode() { return null; }
    @Override public void setLatestChild(TreeNode node) {}
    @Override public void setCurrentResult(Object result) {}
  };

  void push(String name);
  void pop();
  void setCurrentResult(Object result);
  TreeNode getParentNode();
  TreeNode getCurrentNode();
  void setLatestChild(TreeNode node);
}
