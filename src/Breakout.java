import com.almasb.ents.Entity;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.entity.component.TypeComponent;
import com.almasb.fxgl.gameplay.Level;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.parser.TextLevelParser;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.settings.GameSettings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.jbox2d.dynamics.BodyType;
/**
 * Created by 1250493 on 8/8/2016.
 */
public class Breakout extends GameApplication
{
	private GameEntity paddle, ball, back;
	private IntegerProperty bricks, score, orbies, lvl;
	private PaddleControl padCon;
	private BallControl ballCon;
	private TextLevelParser parsa;
	private Level level;
	private boolean gamestart;
	private Background background;

	@Override
	protected void initSettings(GameSettings gameSettings)
	{
		gameSettings.setTitle("Brekout");
		gameSettings.setVersion("0.1");
		gameSettings.setWidth(900);
		gameSettings.setHeight(900);
		gameSettings.setMenuEnabled(false);
		gameSettings.setIntroEnabled(false);

	}

	@Override
	protected void initInput()
	{
		Input input = getInput();

		input.addAction(new UserAction("Left")
		{
			@Override
			protected void onAction()
			{
				padCon.Left();
				if(!gamestart)
					ballCon.left();

			}

			@Override
			protected void onActionEnd()
			{
				padCon.stop();
				if(!gamestart)
					ballCon.stop();
			}
		}, KeyCode.A);

		input.addAction(new UserAction("Right")
		{
			@Override
			protected void onAction()
			{
				padCon.Right();

				if(!gamestart)
					ballCon.right();

			}

			@Override
			protected void onActionEnd()
			{
				padCon.stop();
				if(!gamestart)
					ballCon.stop();

			}
		}, KeyCode.D);

		input.addAction(new UserAction("SKIP")
		{
			@Override
			protected void onActionBegin()
			{
				getGameWorld().getEntitiesByType(Type.BRICK).forEach(e -> e.removeFromWorld());
			}
		}, KeyCode.C);
		input.addAction(new UserAction("StartGame")
		{
			@Override
			protected void onActionBegin()
			{
				if(!gamestart)
				{
					ballCon.start(input.getMousePositionUI());
					gamestart = true;
				}


			}
		}, MouseButton.PRIMARY);

	}

	@Override
	protected void initAssets()
	{
		getAssetLoader().cache();
	}

	@Override
	protected void initGame()
	{
		gamestart = false;

		score = new SimpleIntegerProperty();
		orbies = new SimpleIntegerProperty(3);

		parsa = new TextLevelParser();
		parsa.addEntityProducer('B', (x,y) -> EntityFactory.newBasicBrick(x*64, y*32+30));
		parsa.addEntityProducer('S', (x,y) -> EntityFactory.newStrongBrick(x*64, y*32+30));
		parsa.addEntityProducer('Q', (x,y) -> EntityFactory.newQuickBrick(x*64, y*32+30));
		parsa.addEntityProducer('P', (x,y) -> EntityFactory.newSpawnBrick(x*64, y*32+30));

		lvl = new SimpleIntegerProperty(0);
		level = parsa.parse("breakout" + lvl.get() + ".txt");


        back = new GameEntity();
        back.setViewFromTexture("jail.jpg");
        getGameWorld().addEntity(back);



		initBrick();
		initPaddle();
		initBall();
		initScreenBounds();

	}

	private void initScreenBounds()
	{
		Entity walls = Entities.makeScreenBounds(10);
		walls.addComponent((new TypeComponent(Type.SCREEN)));
		walls.addComponent(new CollidableComponent(true));

		GameEntity bott = Entities.builder().type(Type.BOTTOM).build();
		bott.setY(getHeight()-1);
		bott.getBoundingBoxComponent().addHitBox(new HitBox("BODY", BoundingShape.box(getWidth(), 150)));
		PhysicsComponent phys = new PhysicsComponent();
		phys.setBodyType(BodyType.STATIC);
		bott.addComponent(phys);
		bott.addComponent(new CollidableComponent(true));


		getGameWorld().addEntities(bott, walls);
	}

	private void initPaddle()
	{
		paddle = Entities.builder().type(Type.PADDLE).build();
		paddle.setX(getWidth()/2- 128/2);
		paddle.setY(getHeight()-25);
		paddle.setViewFromTextureWithBBox("paddle.png");
		PhysicsComponent phys = new PhysicsComponent();
		phys.setBodyType(BodyType.KINEMATIC);
		paddle.addComponent(phys);
		paddle.addComponent(new CollidableComponent(true));

		padCon = new PaddleControl();
		paddle.addControl(padCon);


		getGameWorld().addEntity(paddle);
	}

	private void initBall()
	{


		PositionComponent comp = paddle.getPositionComponent();

		double x = comp.getX()+128/2-12;
		double y = comp.getY()-24;

		ball = EntityFactory.newBall(x, y);
		ballCon = ball.getControlUnsafe(BallControl.class);
		getGameWorld().addEntity(ball);
	}
	private void initBrick()
	{
		level.getEntities()
				.stream()
				.forEach(getGameWorld()::addEntity);


	}

	@Override
	protected void initPhysics()
	{
		getPhysicsWorld().setGravity(0, 0);

		getPhysicsWorld().addCollisionHandler(new BallBrickHandler());

		getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.BALL, Type.PADDLE)
		{

			@Override
			protected void onCollisionBegin(Entity a, Entity b)
			{
				PhysicsComponent phys = ball.getComponentUnsafe(PhysicsComponent.class);

				double x = phys.getLinearVelocity().getX();
				double y = phys.getLinearVelocity().getY();

				if(ball.getX() + 24/2 > paddle.getX() + 128/2)
					x = Math.abs(x);
				else
					x = -Math.abs(x);

				phys.setLinearVelocity(x, y);
			}
		});

		getPhysicsWorld().addCollisionHandler(new CollisionHandler(Type.BALL, Type.BOTTOM)
		{
			@Override
			protected void onCollisionBegin(Entity a, Entity b)
			{
				resetBall();
				orbies.add(-1);
				orbies.set(orbies.get()-1);
			}
		});
	}

	@Override
	protected void initUI()
	{
		Text scoreT = new Text();
		scoreT.setTranslateY(20);
		scoreT.setTranslateX(5);
		scoreT.textProperty().bind(score.asString("Score: %d"));
		scoreT.setFont(Font.font(18));
        scoreT.setFill(Color.WHITE);


		Font font = new Font("Comic Sans MS", 24);
		scoreT.setFont(font);

		Text ballsT = new Text();
		ballsT.setTranslateX(getWidth()-125);
		ballsT.setTranslateY(20);
		ballsT.setFont(font);
		ballsT.textProperty().bind(orbies.asString("Orbies: %d"));
        ballsT.setFill(Color.WHITE);

		getGameScene().addUINodes(scoreT, ballsT);
	}

	@Override
	protected void onUpdate(double v)
	{
		checkBricks();
		checkBalls();

	}
	private void resetBall()
	{
		ball.removeFromWorld();
		gamestart = false;
		initBall();
	}
	private void checkBricks()
	{
		if(getGameWorld().getEntitiesByType(Type.BRICK).isEmpty())
		{
			lvl.set(lvl.get()+1);
//			try
//			{
//				parsa.parse("breakout" + lvl.get() + ".txt");
//			}
//			catch(Exception e)
//			{
//				gameOver();
//			}
			level = parsa.parse("breakout" + lvl.get() + ".txt");
			initBrick();
			resetBall();

		}
	}

	private void checkBalls()
	{
		if(orbies.get() <= 0)
			gameOver();
		else if(getGameWorld().getEntitiesByType(Type.BALL).isEmpty())
			resetBall();
	}



	private void gameOver()
	{
		int fontSize = 32;

		Text gameO = new Text("Game Over!");
		int xOffSet = gameO.getText().length() / 4 * fontSize;

		Font font = new Font("Comic Sans MS", 32);
		gameO.setFont(font);
        gameO.setFill(Color.WHITE);

		gameO.setX(getWidth()/2 - xOffSet);
		gameO.setY(getHeight()/2);

        getGameScene().addUINodes(gameO);

        paddle.removeAllControls();

	}

	public static void main(String[] args)
	{
		launch(args);
	}

	public int getScore()
	{
		return score.get();
	}

	public void setScore(int score)
	{
		this.score.set(score);
	}

	public BallControl getBallCon()
	{
		return ballCon;
	}
}
