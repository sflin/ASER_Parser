package parser;

import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;

public class CastVisitor extends AbstractTraversingNodeVisitor<Void, Void> {

	FileCreatorService service;
	
	public CastVisitor() {
		service = new FileCreatorService("/home/selin/archive");
	}
	@Override
	public Void visit(ICastExpression expr, Void context) {
		service.addCastMethod(expr.getTargetType());
		return null;
	}
}