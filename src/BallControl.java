import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.LocalTimer;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 1250493 on 8/16/2016.
 */
public class BallControl extends AbstractControl
{
	private int speed, defaultSpeed;
	private PhysicsComponent phys;
	private PositionComponent pos;
	private boolean hHorC;
	private List<SpeedComponent> speedComponentList;


	public BallControl(int speed )
	{
		defaultSpeed = speed;
		this.speed = speed;
		speedComponentList = new LinkedList<SpeedComponent>();


	}
	@Override
	public void onUpdate(Entity entity, double v)
	{
		updateSpeed();

		if(hHorC)
		{
			if(Math.abs(phys.getLinearVelocity().getX())  != speed)
				phys.setLinearVelocity(Math.signum(phys.getLinearVelocity().getX())* speed, phys.getLinearVelocity().getY());

			if(Math.abs(phys.getLinearVelocity().getY()) != speed)
				phys.setLinearVelocity(phys.getLinearVelocity().getX(), Math.signum(phys.getLinearVelocity().getY())* speed);
		}
		else
		{
			if(Math.abs(phys.getLinearVelocity().getX())  < 0.1)
				phys.setLinearVelocity(Math.signum(phys.getLinearVelocity().getX())* 1.1, phys.getLinearVelocity().getY());

			if(Math.abs(phys.getLinearVelocity().getY()) < 0.1)
				phys.setLinearVelocity(phys.getLinearVelocity().getX(), Math.signum(phys.getLinearVelocity().getY())* 1.1);
		}


	}

	private void updateSpeed()
	{
		// get rid of completed speed component;
		speedComponentList = speedComponentList.stream().filter(e -> !e.timerDone()).collect(Collectors.toList());

		speed = defaultSpeed;

		if(!speedComponentList.isEmpty())
			speedComponentList.forEach(e -> speed *= e.getValue());

		System.out.println(speed);
	}


    public int getSpeed()
	{
		return speed;
	}

	public void setSpeed(SpeedComponent speed)
	{
		speedComponentList.add(speed);
	}

    public void setSpeed(int sp)
    {
        speed = sp;
    }

	public void setHHorC(boolean hHorC)
	{
		this.hHorC = hHorC;
	}

	public void left()
	{
		if(pos.getX() >= speed+128/2-12)
			phys.setLinearVelocity(-speed, 0);
		else
			stop();
	}

	public void right()
	{
		if(pos.getX() <= 900- speed-128/2+12)
			phys.setLinearVelocity(speed, 0);
		else
			stop();
	}

	public void stop()
	{
		phys.setLinearVelocity(0,0);
	}

	public void start(Point2D mouse)
	{
		double x = mouse.getX()- pos.getX();
		double y = mouse.getY()- pos.getY();

		double lineCircle = Math.atan2(y,x);

		//x = x - pos.getX() < 0 ? -speed : speed;
		x = Math.cos(lineCircle)*2;
		y = Math.sin(lineCircle)*2;

		phys.setLinearVelocity(x*speed, y*speed);
	}

	@Override
	public void onAdded(Entity entity)
	{
		phys = entity.getComponentUnsafe(PhysicsComponent.class);
		pos = entity.getComponentUnsafe(PositionComponent.class);
	}
}
