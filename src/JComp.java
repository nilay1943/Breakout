import com.almasb.ents.component.DoubleComponent;
import com.almasb.ents.component.IntegerComponent;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;
import sun.management.jdp.JdpJmxPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by Nilay on 9/29/2016.
 */
public class JComp  extends IntegerComponent
{
    private ArrayList<Winder> frames;
    private int points;


    public JComp(int value)
    {
        super(value);
        resetFrames(value);
        points = 0;
    }

    public void resetFrames(int val)
    {
        frames = new ArrayList<Winder>();
        for (int i = 0; i < val; i++)
        {
            frames.add(new Winder());
        }
    }
    public void hit()
    {
        frames.forEach(e ->
        {
            e.setVisible(true);
            e.requestFocus();
        });

//        LocalTimer timer = FXGL.newLocalTimer();
//        timer.capture();

        int sub = 5;

        Winder wind = new Winder();
        wind.makePos();

	    while(Integer.parseInt(wind.getButtName()) >= sub)
	    {
            try
		    {
			    Thread.sleep(50);
		    }
            catch (InterruptedException e1)
		    {
			    e1.printStackTrace();
		    }

            wind.setButtName((Integer.parseInt(wind.getButtName()) - sub) + "");

		    frames.stream().filter(Winder::isVisible).forEach(e ->
		    {
                e.setButtName((Integer.parseInt(e.getButtName()) - sub) + "");


                Color color = e.getButtColor();


                if(color.getGreen()>sub)
                    e.setButtColor(new Color(0, color.getGreen()-sub, 0));
                else if(color.getRed()>sub)
                    e.setButtColor(new Color(color.getRed()-sub, 0, 0));

		    });
	    }



    }

    public void destroy()
    {
        frames.forEach(Winder::dispose);
    }

    public int getPoints()
    {
        return frames.stream()
                .filter(e -> !e.isVisible())
                .mapToInt(Winder::getPoints)
                .sum();
    }





}
