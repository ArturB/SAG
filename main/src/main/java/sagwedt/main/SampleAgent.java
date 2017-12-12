package sagwedt.main;

import jade.core.*;

public class SampleAgent extends Agent {
 
  /**
	 * 
	 */
	private static final long serialVersionUID = 55182624475616215L;

	protected void setup() {
		// Printout a welcome message
		System.out.println("Hello! Is sample agent alive?: " + this.isAlive() );
	}
}