package parser.visitor;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import parser.addition.CastAdder;
import parser.addition.MethodAdder;
import parser.addition.PropertyAdder;
import parser.addition.SimpleMethodAdder;

public class ContextVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	MethodAdder adder;
	String path;

	public ContextVisitor(String path) {
		this.path = path;
	}

	@Override
	public Void visit(IInvocationExpression expr, Void context) {
		adder = new SimpleMethodAdder(path);
		IMethodName method = expr.getMethodName();
		if (!method.getName().contains("?")) {
			adder.addMethod(method);
		}
		return null;
	}

	@Override
	public Void visit(ICastExpression expr, Void context) {
		adder = new CastAdder(path);
		if (!expr.getTargetType().getName().contains("?")) {
			adder.addMethod(expr.getTargetType());
		}
		return null;
	}

	@Override
	public Void visit(IPropertyReference expr, Void context) {
		adder = new PropertyAdder(path);
		adder.addMethod(expr.getPropertyName());
		return null;
	}
}