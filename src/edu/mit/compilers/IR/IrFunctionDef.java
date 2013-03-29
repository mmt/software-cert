package edu.mit.compilers.IR;

import java.util.List;
import java.util.ArrayList;

public class IrFunctionDef extends IrNode {
  IrType mType;
  IrIdentifier mName;
  List<IrDeclaration> mParams; 
  
  public IrFunctionDef(IrType type, IrIdentifier name) {
    mType = type;
    mName = name;
    mParams = new ArrayList<IrDeclaration>();
  }
  
  public void addParam(IrDeclaration param) {
    mParams.add(param);
  }
  
  @Override
  public void accept(IrNodeVisitor v) {
    v.visit(this);
  }
}
