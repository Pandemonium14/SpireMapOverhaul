package spireMapOverhaul.zones.glassblower;

import com.badlogic.gdx.graphics.Color;
import spireMapOverhaul.abstracts.AbstractZone;

public class GlassblowerZone extends AbstractZone {

    public static final String ID = "Glassblower";

    public GlassblowerZone() {
        super(ID, Icons.MONSTER, Icons.ELITE, Icons.REWARD, Icons.REST);
        width = 4;
        height = 4;
    }


    @Override
    public AbstractZone copy() {
        return new GlassblowerZone();
    }

    @Override
    public Color getColor() {
        return new Color(0.85f,0.85f,0.9f,1f);
    }
}
