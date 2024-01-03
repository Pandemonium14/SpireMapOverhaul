package spireMapOverhaul.zones.glassblower.campfire;


import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;

public class BottleCardOption extends AbstractCampfireOption {
    private static final UIStrings strings = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID("BottleCardCampfireOption"));
    private static final Texture TEXTURE = TexLoader.getTexture(SpireAnniversary6Mod.makeUIPath("BottleCampfire.png"));

    public BottleCardOption(boolean active) {
        this.usable = active;
        label = strings.TEXT[0];
        description = strings.TEXT[1];
        img = TEXTURE;
    }

    @Override
    public void useOption() {

        AbstractDungeon.effectsQueue.add(new BottleCardEffect());
    }
}
