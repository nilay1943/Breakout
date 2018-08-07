import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PaddleControl extends AbstractControl
{
    private PositionComponent position;
    private PhysicsComponent physics;

    @Override
    public void onAdded(Entity entity)
    {
        position = entity.getComponentUnsafe(PositionComponent.class);
        physics = entity.getComponentUnsafe(PhysicsComponent.class);
    }

    @Override
    public void onUpdate(Entity entity, double v) {}

    public void Left()
    {
        if(position.getX() >= 5)
            physics.setLinearVelocity(-8, 0);
        else
            stop();
    }

    public void Right()
    {
        if(position.getX() <= 900 - 128 - 5)
            physics.setLinearVelocity(8, 0);
        else
            stop();
    }

    public void stop()
    {
        physics.setLinearVelocity(0, 0);
    }
}
