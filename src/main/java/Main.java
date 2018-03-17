import java.util.Set;

import com.google.common.collect.Sets;

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.kave.commons.utils.io.json.JsonUtils;
import cc.kave.commons.utils.ssts.SSTPrintingUtils;

@SuppressWarnings("unused")
public class Main {

	private static final String DIR_CONTEXTS = "C:\\temp\\Contexts\\";

	public static void main(String[] args){
		readContextsFromDisk();
		
	}
	/**
	 * 1: read contexts
	 */
	public static void readContextsFromDisk() {
		for (Context ctx : IoHelper.readAll(DIR_CONTEXTS)) {
			printSST(accessIRElements(ctx));		
		}
	}

	/**
	 * 2: access the different elements of a context
	 */
	public static ISST accessIRElements(Context ctx) {
		// get information from type system
		ITypeShape ts = ctx.getTypeShape();

		// this includes information about type hierarchy, e.g., which
		// interfaces are implemented
		ITypeHierarchy typeHierarchy = ts.getTypeHierarchy();

		// you can access the "simplified syntax tree" (SST), our intermediate
		// representation, which includes the normalized representation of a
		// class
		ISST sst = ctx.getSST();
		
		return sst;
	}

	/**
	 * 3: print an SST to the terminal
	 */
	public static void printSST(ISST sst) {
		System.out.println(SSTPrintingUtils.printSST(sst));
	}

	/**
	 * 4: parse an existing SST, add a method, and serialize it again
	 */
	public static void parseChangeAndSerialize() {
		// read an ISST from a string
		ISST sst = JsonUtils.fromJson("...", ISST.class);

		// define new method declaration
		MethodDeclaration md = new MethodDeclaration();
		// set fully-qualified name of method
		md.setName(Names.newMethod("[ReturnType, MyProject] [DeclaringType, MyProject].methodName()"));
		// add a statement to its body, e.g. a return statement
		md.getBody().add(new ReturnStatement());
		// add the new declaration to the set of methods
		sst.getMethods().add(md);

		// serialize the result back to a Json representation
		String json = JsonUtils.toJson(ISST.class);
	}

	/**
	 * 6: apply the inlining transformation to an SST
	 */
	public static void applyInlining(ISST originalSst) {
		// initialize the inlining
		InliningContext context = new InliningContext();
		// start the transformation by accepting the visitor
		originalSst.accept(context.getVisitor(), context);
		// and access the inlined version
		ISST inlinedSst = context.getSST();
	}

	/**
	 * 7: perform a points-to analysis
	 * 
	 * Names are a bit ambiguous in this example.
	 * 
	 * The parameter "originalContext" points to the class "Context", which
	 * represents a class (incl. the IR of the source code, and information
	 * about the type sytem).
	 * 
	 * The class "PointsToContext" refers to the result of the points-to
	 * analysis and which can be used to find abstract locations.
	 */
	public static void performPointsTo(Context originalContext) {
		// pick an implementation for a pointer analysis
		PointsToAnalysis pa = new UnificationAnalysis(FieldSensitivity.FULL);

		// run the analysis
		PointsToContext ptRes = pa.compute(originalContext);

		// create a helper for building the queries
		PointsToQueryBuilder queryBuilder = new PointsToQueryBuilder(ptRes);

		// assume we find an invocation expressions somewhere in the SST...
		IInvocationExpression ie = null;
		// ...together with its parent statement
		IStatement stmt = null;

		// we are interested to find the instances that are used as the receiver
		// or as a parameter of this invocation

		// create a query for the receiver of the invocation...
		PointsToQuery queryForReceiver = queryBuilder.newQuery(ie.getReference(), stmt);
		// ... and query for the abstract locations (instances)
		Set<AbstractLocation> receivers = pa.query(queryForReceiver);

		// now iterate over all parameters
		for (IParameterName p : ie.getMethodName().getParameters()) {
			// again, create a query...
			PointsToQuery queryForParam = queryBuilder.newQuery(varRef(p.getName()), stmt);
			// ... and query for the abstract locations
			Set<AbstractLocation> parameters = pa.query(queryForParam);
		}

		// do something with the results
	}

	private static IReference varRef(String name) {
		VariableReference vr = new VariableReference();
		vr.setIdentifier(name);
		return vr;
	}
}