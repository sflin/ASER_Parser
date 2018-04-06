package examples;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
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

@SuppressWarnings("unused")
public class Main {

	private static final String DIR_CONTEXTS = "/home/selin/Documents/Contexts-170503/Antaris";

	public static List<String> constructors = new ArrayList<String>();
	public static List<String> methods = new ArrayList<String>();

	public static void main(String[] args){
		readContextsFromDisk();

		System.out.print("constructors:\n");
		System.out.println(constructors);
		System.out.println(constructors.size());
		System.out.print("methods:\n");
		System.out.println(methods);
		System.out.println(methods.size());
		
	}
	/**
	 * 1: read contexts
	 */
	public static void readContextsFromDisk() {
//		Context ctx = IoHelper.readFirstContext(DIR_CONTEXTS);
//		accessIRElements(ctx);
		List<String> finallist = new ArrayList<String>();
//		applyInlining(accessIRElements(ctx));
		for (Context ctx : IoHelper.readAll(DIR_CONTEXTS)) {
//			performPointsTo(ctx);
			accessIRElements(ctx);
			
//			applyInlining(accessIRElements(ctx));
//			printSST(accessIRElements(ctx));
//			printSST(ctx);
		}
		System.out.println(finallist);
		System.out.println(finallist.size());
	}
		/**
	 * 2: access the different elements of a context
	 */
	public static ISST accessIRElements(Context ctx) {
		
//		GraphicsEnvironment ge = GraphicsEnvironment.
//				getLocalGraphicsEnvironment();
//				GraphicsDevice myDevice =
//				ge.getDefaultScreenDevice();
//				DisplayMode dm = 
		
		// get information from type system
		ITypeShape ts = ctx.getTypeShape();

		// this includes information about type hierarchy, e.g., which
		// interfaces are implemented
		ITypeHierarchy typeHierarchy = ts.getTypeHierarchy();

		// you can access the "simplified syntax tree" (SST), our intermediate
		// representation, which includes the normalized representation of a
		// class
		ISST sst = ctx.getSST();
//		List<String> constructors = new ArrayList<String>();
//		List<String> methods = new ArrayList<String>();
		
		for(IMethodDeclaration method : sst.getMethods()) {
			for(IStatement stmt : method.getBody()) {
				if(stmt instanceof Assignment) {
					IAssignableExpression expr = ((Assignment) stmt).getExpression();
					if(expr instanceof InvocationExpression) {
//						System.out.println("--------");
//						System.out.println("Class: " + sst.getEnclosingType().getFullName());
//						System.out.println(expr + "\n\n");
//					System.out.println(((InvocationExpression) expr).getMethodName() + "");	
					IMethodName met = ((InvocationExpression) expr).getMethodName();
//					System.out.println("\nFullName: " + met.getFullName()); 
					if(met.getFullName().contains(".ctor")) {
						String tmp = met.getDeclaringType().getName() + "(";
//						System.out.print("new " + met.getDeclaringType().getName() + "(");
						Iterator<IParameterName> iter = met.getParameters().iterator();
						while(iter.hasNext()) {
							IParameterName param = iter.next();
//							System.out.print(param.getValueType().getName() + " " + param.getName());
							tmp += param.getValueType().getName() + " " + param.getName();
							if(iter.hasNext()) {
//								System.out.print(", ");
								tmp += ", ";
							}
						}
						tmp += ")";
						if(!constructors.contains(tmp)) {
							constructors.add(tmp);
						}
					}
					else {
							// System.out.println("\nParams: " + met.getParameters());
							// System.out.println("\nDeclaring Type: " + met.getDeclaringType().getName());
							// System.out.println("\nIdentifier: " + met.getIdentifier());
							// System.out.println("\nReturnType: " + met.getReturnType());
							// System.out.println("\nValueType: " + met.getValueType());
							// System.out.println("\nName: " + met.getName());
							// System.out.print(met.getDeclaringType().getName() + "." +
							// met.getFullName()+"("); // getReturnType().getFullName()
							String tmp = met.getDeclaringType().getName() + "." + met.getName() + "(";
							Iterator<IParameterName> iter = met.getParameters().iterator();
							while (iter.hasNext()) {
								IParameterName param = iter.next();
								// System.out.print(param.getValueType().getFullName() + " " + param.getName());
								tmp += param.getValueType().getName() + " " + param.getName();
								if (iter.hasNext()) {
									// System.out.print(", ");
									tmp += ", ";
								}
							}
							tmp += ")";
							if (!methods.contains(tmp)) {
								methods.add(tmp); // TODO: add counter here?
							}
						}
//					System.out.println(")");
//					System.out.println("\n--------");
					}
				}
//				System.out.println("\n--------\n" + stmt);
			}
		}
		return sst;
	}

	/**
	 * 3: print an SST to the terminal
	 */
	public static void printSST(ISST sst) {
		System.out.println(sst);
//		System.out.println(SSTPrintingUtils.printSST(sst));
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
		printSST(inlinedSst);
//		System.out.println(inlinedSst);
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
//		System.out.println(ptRes.toString());

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
			System.out.println(parameters);
		}
		System.out.println("-------\nreceivers:\n");
		System.out.println(receivers);

		// do something with the results
	}

	private static IReference varRef(String name) {
		VariableReference vr = new VariableReference();
		vr.setIdentifier(name);
		return vr;
	}
}