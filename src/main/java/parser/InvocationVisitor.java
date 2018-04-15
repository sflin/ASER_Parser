package parser;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;


public class InvocationVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	FileService service;
	
	public InvocationVisitor(String path) {
		service = new FileService(path);
	}
	@Override
	public Void visit(IInvocationExpression expr, Void context) {
		IMethodName method = expr.getMethodName();
		service.addMethod(method);
		return null;
	}
}