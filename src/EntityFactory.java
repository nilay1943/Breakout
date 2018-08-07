import com.almasb.ents.Component;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.badlogic.gdx.Game;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import java.util.List;

/**
 * Created by 1250493 on 8/25/2016.
 */
public class EntityFactory
{
	private static GameEntity newBrick(double x, double y)
	{
		PhysicsComponent phys = new PhysicsComponent();
		phys.setBodyType(BodyType.STATIC);

		return Entities.builder()
				.at(x, y)
				.type(Type.BRICK)
				.with(phys, new CollidableComponent(true))
				.build();
	}

	public static GameEntity newBall(double x, double y)
	{
		PhysicsComponent phys = new PhysicsComponent();
		phys.setBodyType(BodyType.DYNAMIC);

		FixtureDef def = new FixtureDef();
		def.setDensity(0.1f);
		def.setRestitution(1.0f);

		phys.setFixtureDef(def);

		return Entities.builder()
				.at(x, y)
				.type(Type.BALL)
				.viewFromTextureWithBBox("ball.png")
				.bbox(new HitBox("BODY", BoundingShape.circle(12)))
				.with(phys, new CollidableComponent(true))
				.with(new BallControl(8))
				.build();
	}

	public static GameEntity newBasicBrick(double x, double y)
	{
		BrickType type = BrickType.BASIC;

		GameEntity brick = newBrick(x, y);
		brick.addComponent(new SubTypeComponent(type));
		brick.setViewFromTextureWithBBox(type.texture);
		brick.addComponent(new HPComponent(1));
		return brick;
	}

	public static GameEntity newStrongBrick(double x, double y)
	{
		BrickType type = BrickType.STRONGBRICK;

		GameEntity brick = newBrick(x, y);
		brick.addComponent(new SubTypeComponent(type));
		brick.setViewFromTextureWithBBox(type.texture);
		brick.addComponent(new HPComponent(2));

		return brick;
	}

	public static GameEntity newQuickBrick(double x, double y)
	{
		BrickType type = BrickType.SPEEDBRICK;

		GameEntity brick = newBrick(x, y);
		brick.addComponent(new SubTypeComponent(type));
		brick.setViewFromTextureWithBBox(type.texture);
		brick.addComponent(new HPComponent(1));
		brick.addComponent(new SpeedComponent(1.2, 5000));

		return brick;
	}

	public static GameEntity newSpawnBrick(double x, double y)
	{
		BrickType type = BrickType.SPAWNBRICK;

		GameEntity brick = newBrick(x, y);
		brick.addComponent(new SubTypeComponent(type));
		brick.setViewFromTextureWithBBox(type.texture);
		brick.addComponent(new HPComponent(1));
		brick.addComponent(new JComp(20));
		return brick;
	}
}
