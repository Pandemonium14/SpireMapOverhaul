package spireMapOverhaul.zones.glassblower.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StasisPower;
import spireMapOverhaul.zones.glassblower.monsters.GlassGolem;
import spireMapOverhaul.zones.glassblower.powers.StasisIdOffsetPower;

public class StealCardAction extends AbstractGameAction {

    private final GlassGolem owner;
    private AbstractCard card;

    public StealCardAction(GlassGolem owner) {
        this.owner = owner;
        duration = startDuration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.isEmpty() && AbstractDungeon.player.discardPile.isEmpty()) {
            this.isDone = true;
        } else {
            if (this.duration == this.startDuration) {
                //todo : prevent it stealing the last attack card
                if (!AbstractDungeon.player.drawPile.isEmpty()) {
                    this.card = AbstractDungeon.player.drawPile.getTopCard();
                    AbstractDungeon.player.drawPile.removeCard(this.card);
                } else if (!AbstractDungeon.player.discardPile.isEmpty()) {
                    this.card = AbstractDungeon.player.discardPile.getRandomCard(AbstractDungeon.cardRandomRng);
                    AbstractDungeon.player.discardPile.removeCard(card);
                } else {
                    isDone = true;
                    return;
                }

                AbstractDungeon.player.limbo.addToBottom(this.card);
                this.card.setAngle(0.0F);
                this.card.targetDrawScale = 0.75F;
                this.card.target_x = (float)Settings.WIDTH / 2.0F;
                this.card.target_y = (float)Settings.HEIGHT / 2.0F;
                this.card.lighten(false);
                this.card.unfadeOut();
                this.card.unhover();
                this.card.untip();
                this.card.stopGlowing();
            }

            this.tickDuration();
            if (isDone && card != null) {
                addToTop(new ApplyPowerAction(this.owner, this.owner, new StasisIdOffsetPower(this.owner, this.card)));
                addToTop(new ShowCardAction(this.card));
            }

        }
    }

}
