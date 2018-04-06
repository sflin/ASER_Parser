import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;


public class InvocationVisitor extends AbstractTraversingNodeVisitor<InvocationVisitorContext, List<String>> {

	List<String> names;
	
	public InvocationVisitor() {
		names = new ArrayList<String>();
	}
	@Override
	public List<String> visit(IInvocationExpression expr, InvocationVisitorContext context) {
//		expr.getReference().accept(this, context); 
		// TODO: implement logic for saving of "context-files"
		// getReference()??? und getParameters()?
//		if(expr.getMethodName().isConstructor()){
//	 add method as constructor	
//	}
		String method = expr.getMethodName().getName();
		context.addMethod(method);
//		if(!names.contains(method)){
//			names.add(method);
//		}
//		System.out.println(names);
		return names;
	}
	
//	@Override
//	public Void visit(IVariableReference ref, InvocationVisitorContext context) {
//		System.out.println(ref.getIdentifier());
//		return null;
//	}
}