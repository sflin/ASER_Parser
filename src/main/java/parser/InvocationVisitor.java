package parser;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;


public class InvocationVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	FileCreatorService service;
	
	public InvocationVisitor() {
		service = new FileCreatorService(System.getProperty("user.home") + "/archive");
	}
	@Override
	public Void visit(IInvocationExpression expr, Void context) {
		IMethodName method = expr.getMethodName();
		if(!method.getName().contains("?")) {
			service.addMethod(method);
		}
		return null;
	}
}