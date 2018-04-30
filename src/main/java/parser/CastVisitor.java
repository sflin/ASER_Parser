package parser;

import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;

public class CastVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	FileCreatorService service;

	public CastVisitor() {
		service = new FileCreatorService(System.getProperty("user.home") + "/archive");
	}

	@Override
	public Void visit(ICastExpression expr, Void context) {
		if (!expr.getTargetType().getName().contains("?")) {
			service.addCastMethod(expr.getTargetType());
		}
		return null;
	}
}