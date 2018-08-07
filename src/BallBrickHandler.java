import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.event.FXGLEvent;
import com.almasb.fxgl.physics.CollisionHandler;

/**
 * Created by 1250493 on 9/15/2016.
 */


public class BallBrickHandler extends CollisionHandler
{

	private Breakout app;
	public BallBrickHandler()
	{
		super(Type.BALL, Type.BRICK);
		app = (Breakout) FXGL.getApp();
	}

	@Override
	protected void onCollisionBegin(Entity ball, Entity rick)
	{

		HPComponent hp = rick.getComponentUnsafe(HPComponent.class);
		SpeedComponent sped = rick.getComponentUnsafe(SpeedComponent.class);
		JComp frame = rick.getComponentUnsafe(JComp.class);
		hp.setValue(hp.getValue()-1);

		if(hp.getValue() == 1)
			((GameEntity) rick).setViewFromTextureWithBBox("brick.png");

		if(hp.getValue() <= 0)
		{
			rick.removeFromWorld();
			app.setScore(app.getScore() + 100);
		}

		if(sped != null)
		{
			sped.startTimer();
			ball.getControlUnsafe(BallControl.class).setSpeed(sped);

		}

		if(frame != null)
        {
            frame.hit();

//            try
//            {
//                Thread.sleep(2000);
//            } catch (InterruptedException e)
//            {
//                e.printStackTrace();
//            }
			System.out.println("No");
			app.setScore(app.getScore()+frame.getPoints());

			System.out.println("did it");
			frame.destroy();
        }

		ball.getControlUnsafe(BallControl.class).setHHorC(true);
	}


}
