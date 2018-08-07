import com.almasb.ents.component.DoubleComponent;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.time.LocalTimer;
import javafx.util.Duration;

/**
 * Created by 1250493 on 9/20/2016.
 */
public class SpeedComponent extends DoubleComponent
{
	private int duration;
	private LocalTimer timer;

	public SpeedComponent(double value, int duration)
	{
		super(value);
		this.duration = duration;
		timer = FXGL.newLocalTimer();
	}

	public boolean timerDone()
	{
		return timer.elapsed(Duration.millis(duration));
	}

	public void startTimer()
	{
		timer.capture();
	}




}
