package parser;

import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;

public class CastVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	FileCreatorService service;

	public CastVisitor(String path) {
		service = new FileCreatorService(path);
	}

	@Override
	public Void visit(ICastExpression expr, Void context) {
		if (!expr.getTargetType().getName().contains("?")) {
			service.addCastMethod(expr.getTargetType());
		}
		return null;
	}
}