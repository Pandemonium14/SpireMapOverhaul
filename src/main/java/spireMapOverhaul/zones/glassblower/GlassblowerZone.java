package spireMapOverhaul.zones.glassblower;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zones.glassblower.campfire.BottleCardOption;

import java.util.ArrayList;

public class GlassblowerZone extends AbstractZone implements CampfireModifyingZone {

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

    @Override
    public void postAddButtons(ArrayList<AbstractCampfireOption> buttons) {
        boolean canUse = AbstractDungeon.player.currentHealth > (int) (AbstractDungeon.player.maxHealth * 0.15f);
        buttons.add(new BottleCardOption(canUse));
    }

    @Override
    public void postUseCampfireOption(AbstractCampfireOption option) {
        if (option instanceof BottleCardOption) {

        }
    }
}
