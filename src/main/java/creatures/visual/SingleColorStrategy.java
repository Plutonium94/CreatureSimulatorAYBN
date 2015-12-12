package creatures.visual;

import creatures.IColorStrategy;
import java.awt.Color;

/**
*	Returns same color each time
*/

public class SingleColorStrategy implements IColorStrategy {

	private Color color;


	public SingleColorStrategy(Color c) {
		color = c;
	}

	public SingleColorStrategy(String colorName) {
		try {
			this.color = (Color) Color.class.getDeclaredField(colorName.toUpperCase()).get(null);
		} catch(NoSuchFieldException nfse) {
			System.err.println("Error converting colorName " +colorName+" to color");
			nfse.printStackTrace();
		} catch(SecurityException se) {
			se.printStackTrace();
		} catch(IllegalArgumentException ilarge) {
			ilarge.printStackTrace();
		} catch(IllegalAccessException iacce) {
			iacce.printStackTrace();
		}
	}

	@Override
	public Color getColor() {
		return color;
	}
}