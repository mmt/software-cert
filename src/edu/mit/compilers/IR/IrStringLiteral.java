package edu.mit.compilers.IR;

public class IrStringLiteral extends IrNode {
  @Override
  public void accept(IrNodeVisitor v) {
    v.visit(this);
  }

  @Override
  public IrNode copy() {
    // TODO Auto-generated method stub
    return null;
  }
}
