package spireMapOverhaul.zones.glassblower;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.zoneInterfaces.CampfireModifyingZone;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.EncounterModifyingZone;
import spireMapOverhaul.zones.glassblower.campfire.BottleCardOption;
import spireMapOverhaul.zones.glassblower.monsters.GlassGolem;

import java.util.ArrayList;
import java.util.List;

public class GlassblowerZone extends AbstractZone implements CampfireModifyingZone, EncounterModifyingZone {

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
    public List<AbstractMonster> getAdditionalMonsters() {
        ArrayList<AbstractMonster> l = new ArrayList<>();
        l.add(new GlassGolem());
        return l;
    }
}
