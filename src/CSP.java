import choco.Choco;
import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerExpressionVariable;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;

import static choco.Choco.*;


/* 
 * Yassine
 * Ait Malek 
 * DSE
 * CSP
 */

public class CSP {
	
	
	
	public CSP() {
		super();

		this.run();
	}


	// Model
    private Model m = new CPModel();
    
    // Solver
    private Solver s = new CPSolver();
    
	//  Parameters
    
	// nb cars
	private Integer v = 10;
	
	// nb options
	private Integer o = 5;
	
	// nb classes
	private Integer c = 6;
	
    // demande
    private Integer d[] = {1,1,2,2,2,2};

    // options properties
    private Integer p[] = {1,2,1,2,1};
    private Integer q[] = {2,3,3,5,5};
    
    
    // options array
	private Integer [][] r = {
						    		{1,0,1,1,0},
						    		{0,0,0,1,0},
						    		{0,1,0,0,1},
						    		{0,1,0,1,0},
						    		{1,0,1,0,0},
						    		{1,1,0,0,0}
							};
	
	// problem variables 
	private IntegerVariable[] car_class ;

	private IntegerVariable[][] classonpos ;  

	
	// initilize problem variables
	public void init() {
		this.car_class = makeIntVarArray("car_class", this.v, 1, this.c);
		this.classonpos =  makeIntVarArray("classonpos", this.o, this.v, 0, 1);
	}
	
	// add constraints to the model
	public void cons() {
		
		// C1 car occurrences 
        for (int j = 0; j < this.c; j++) {
            this.m.addConstraint(Choco.occurrence(d[j], this.car_class, j+1));
        }


        // C2  each car of a class has specific options
        for (int cat = 0; cat < this.c; cat++) {
        	
            for (int car = 0; car < this.v; car++) {
                Constraint[] C = new Constraint[this.o];
                for (int op = 0; op < this.o; op++) {
                	C[op] = eq(this.classonpos[op][car], r[cat][op]);
                }
                    
                m.addConstraint(implies(eq(this.car_class[car], cat+1),and(C)));
            }
        }


        // C3 p q constraints
        for(int i = 0; i < this.o; i++) {
            for(int j = 0; j < this.v-q[i]+1; j++) {
                IntegerExpressionVariable somme = ZERO ;
                for(int c = 0; c < q[i]; c++) {
                    somme = Choco.plus(somme,this.classonpos[i][j+c]);
                }
                m.addConstraint(Choco.leq(somme,p[i]));
            }
        }

	}
	
	// solve the model and the problem
	public void solve() {
		
		// read model
		 this.s.read(m);
		 
		 //solve model
		 this.s.solve();
		 
		 // show results
		 
		 Integer t = 1;
	     do {
	
	            System.out.println("Solution : "+t);
	
	            System.out.println("Class  \t    Required options");
	            for (int i = 0; i < this.v; i++) {
	                System.out.print("  "+(s.getVar(this.car_class[i]).getVal() - 1)+"\t   \t");
	                for (int j = 0; j < this.o; j++) {
	                    System.out.print(s.getVar(this.classonpos[j][i]).getVal() +" ");
	                }
	                System.out.println("");
	            }
	            System.out.println("");
	
	            // number of solutions
	            t++;
	            
	        }while (s.nextSolution());

	}
	
	//  run methode
	public void run( ) {
		
		this.init();
		this.cons();
		this.solve();
	}
	
    public Integer getV() {
		return v;
	}

	public void setV(Integer v) {
		this.v = v;
	}

	public Integer getO() {
		return o;
	}

	public void setO(Integer o) {
		this.o = o;
	}

	public Integer getC() {
		return c;
	}

	public void setC(Integer c) {
		this.c = c;
	}

	public Integer[] getD() {
		return d;
	}

	public void setD(Integer[] d) {
		this.d = d;
	}

	public Integer[] getP() {
		return p;
	}

	public void setP(Integer[] p) {
		this.p = p;
	}

	public Integer[] getQ() {
		return q;
	}

	public void setQ(Integer[] q) {
		this.q = q;
	}

	public Integer[][] getR() {
		return r;
	}

	public void setR(Integer[][] r) {
		this.r = r;
	}

	public static void main(String[] args) {

    		new CSP();
    }
}