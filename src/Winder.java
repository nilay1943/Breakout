import com.almasb.fxgl.app.FXGL;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by Nilay on 9/30/2016.
 */
public class Winder extends JFrame
{
    private JButton butt;

    public Winder()
    {
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.setLocation((int)(Math.random()*900),(int)(Math.random()*900));
        this.setTitle("Error");
        this.setSize(100, 100);
        this.setLayout(new GridLayout());
        this.requestFocus();

        int r = 255;//((int)(Math.random()*224));
        butt = new JButton(r +"");
        butt.setName(r+"");
        butt.setBackground(new Color(0, r, 0));

        if((Math.random()*10) < 4)
        {
            setButtName(-5 + "");
//            butt.setName("-" + butt.getName());
//            butt.setText(butt.getName());
            butt.setBackground(new Color(r ,0 , 0));
        }

        butt.addActionListener(e -> this.setVisible(false));

        this.add(butt);
    }

    public int getPoints()
    {
        int output = Integer.parseInt(butt.getName());

        return output > 0?(output):(output - 100) ;
    }

    public String getButtName()
    {
        return butt.getName();
    }

    public void setButtName(String butt)
    {
        this.butt.setText(butt);
        this.butt.setName(butt);
    }

    public Color getButtColor()
    {
        return butt.getBackground();
    }

    public void setButtColor(Color color)
    {
        butt.setBackground(color);
    }

    public void makePos()
    {
        this.setButtName(255 + "");
    }



}
