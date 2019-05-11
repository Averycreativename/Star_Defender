package stardef.Baseclasses;

import java.io.Serializable;
import javafx.scene.paint.Color;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="Color")
/**
 * This class used to store javafx colors.
 */
public class CustomColor
{
    public double red;
    public double green;
    public double blue;
    public double alpha;

    /**
     *The constructor of CustomColor class.
     * @param color Javafx color variable
     */
    public CustomColor(Color color)
    {
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
        this.alpha = color.getOpacity();
    }

    /**
     * The constructor of CustomColor class, that have no parameters.
     */
    public CustomColor(){}

    /**
     * The constructor of CustomColor class, that needs values between 0 and 1.
     * @param red double value between 0 and 1
     * @param green double value between 0 and 1
     * @param blue double value between 0 and 1
     * @param alpha double value between 0 and 1, it is the opacity
     */
    public CustomColor(double red, double green, double blue, double alpha)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    /**
     * The constructor of CustomColor class, that needs values 0 and 255.
     * @param red integer value between 0 and 255
     * @param green integer value between 0 and 255
     * @param blue integer value between 0 and 255
     */
    public CustomColor(int red, int green, int blue)
    {
        Color c=Color.rgb(red, green, blue);
        this.red=c.getRed();
        this.green=c.getGreen();
        this.blue=c.getBlue();
        this.alpha=1;
    }

    /**
     * javafx color getter.
     * @return converts back CustomColor to javafx color
     */
    public Color getFXColor()
    {
        return new Color(red, green, blue, alpha);
    }
}