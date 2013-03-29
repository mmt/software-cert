package edu.mit.compilers.graphmodel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import edu.mit.compilers.IR.*;

public class GraphPreparer implements IrNodeVisitor {
  private List<IrNode> mInstructions;
  private Map<String, Integer> mNameAssignments;
  private int mNextID;
  
  public List<IrNode> prepare(IrProgram p) {
    mInstructions = new ArrayList<IrNode>();
    mNameAssignments = new HashMap<String, Integer>();
    mNextID = 0;
    
    visit(p);
    return mInstructions;
  }
  
  private int getNextID() {
    return mNextID++;
  }

  @Override
  public void visit(IrAssignment n) {
    IrExpression rhs = n.getValue();
    rhs.accept(this);
    
    if (rhs instanceof IrBinOp) {
      IrIdentifier newValue = new IrIdentifier();
      newValue.setResultAddress(rhs.getResultAddress());
      IrAssignment newAssign = new IrAssignment(n.getTarget(), newValue);
      mInstructions.add(newAssign);
    } else {
      mInstructions.add(n);
    }
  }

  @Override
  public void visit(IrBinOp n) {
    IrExpression oldLeft = n.getLeft();
    IrExpression oldRight = n.getRight();
    IrExpression newLeft, newRight;
    
    int newAddr = getNextID();
    IrIdentifier newTarget = new IrIdentifier();
    newTarget.setResultAddress(newAddr);
    n.setResultAddress(newAddr);
    IrBinOp newOp;
    IrAssignment tempAssign;
    
    
    n.getRight().accept(this);
    
    if (oldLeft instanceof IrBinOp) {
      oldLeft.accept(this);
      newLeft = new IrIdentifier();
      newLeft.setResultAddress(oldLeft.getResultAddress());
    } else {
      newLeft = oldLeft;
    }
    
    if (oldRight instanceof IrBinOp) {
      oldRight.accept(this);
      newRight = new IrIdentifier();
      newRight.setResultAddress(oldRight.getResultAddress());
    } else {
      newRight = oldRight;
    }
    
    newOp = new IrBinOp(newLeft, n.getOp(), newRight);
    tempAssign = new IrAssignment(newTarget, newOp);
    mInstructions.add(tempAssign);
  }

  @Override
  public void visit(IrControlPoint n) {
    // Not generated by above level
  }

  @Override
  public void visit(IrDeclaration n) {
    // Not necessary to handle
  }

  @Override
  public void visit(IrExtFunctionCall n) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void visit(IrFunctionDef n) {
    for (IrNode node : n.getChildren()) {
      node.accept(this);
    }
  }

  @Override
  public void visit(IrIdentifier n) {
    if (mNameAssignments.containsKey(n.getName())) {
      int id = getNextID();
      String name = n.getName();
      mNameAssignments.put(name, id);
      n.setResultAddress(id);
    }
  }

  @Override
  public void visit(IrLiteral n) {
    // Address already assigned
  }

  @Override
  public void visit(IrProgram n) {
    IrFunctionDef main = n.getMain();
    main.accept(this);
  }

  @Override
  public void visit(IrRedacted n) {
    // Ignore
  }

  @Override
  public void visit(IrReturn n) {
    if (n.hasReturn()) {
      
      
      
    } else {
      mInstructions.add(n);
    }
  }

  @Override
  public void visit(IrStringLiteral n) {
    // Should not be visited
  }

  @Override
  public void visit(IrType n) {
    // Should not be visited
  }

  @Override
  public void visit(IrWhile n) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void visit(IrIf n) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void visit(IrLabel n) {
    // Eventually deal with inline labels (goes with goto statements)
  }

  @Override
  public void visit(IrJmp n) {
    // Eventually deal with goto statements
  }

}
