/**
 * Created by 1250493 on 9/13/2016.
 */
public enum BrickType
{
	BASIC("brick.png"), STRONGBRICK("strongbrick.png"), SPEEDBRICK("speedbrick.png"), SPAWNBRICK("spawnbrick.png");

	final String texture;

	BrickType(String texture)
	{
		this.texture = texture;

	}
}
