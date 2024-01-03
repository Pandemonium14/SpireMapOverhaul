package spireMapOverhaul.zones.glassblower.campfire;

import basemod.cardmods.InnateMod;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import spireMapOverhaul.zones.glassblower.patches.MakeDamageMethodUseEffectsQueuePatch;

public class BottleCardEffect  extends AbstractGameEffect {


    private static final float DUR = 1.5F;
    private boolean openedScreen = false;
    private Color screenColor;

    public BottleCardEffect() {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = 1.5F;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }

        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            //todo: do vfx
            CardModifierManager.addModifier(card, new InnateMod());

            AbstractDungeon.topLevelEffectsQueue.add(new AbstractGameEffect() {
                @Override
                public void update() {
                    try {
                        MakeDamageMethodUseEffectsQueuePatch.active = true;
                        AbstractDungeon.player.damage(new DamageInfo(null, (int) (AbstractDungeon.player.maxHealth * 0.15f)));
                    }
                    finally {
                        MakeDamageMethodUseEffectsQueuePatch.active = false;
                    }
                    this.isDone = true;
                }
                @Override
                public void render(SpriteBatch spriteBatch) {}
                @Override
                public void dispose() {}
            });
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        if (this.duration < 1.0F && !this.openedScreen) {
            this.openedScreen = true;
            AbstractDungeon.gridSelectScreen.open(getPossibleCards(), 1, "TO LOCALIZE!!!!!.", false, false, true, true);
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
            if (CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0.0F;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
            }
        }

    }

    private CardGroup getPossibleCards() {
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (!c.isInnate && !c.inBottleFlame && !c.inBottleLightning && !c.inBottleTornado) {
                group.group.add(c);
            }
        }
        return group;
    }

    private void updateBlackScreenColor() {
        if (this.duration > 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float) Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
            AbstractDungeon.gridSelectScreen.render(sb);
        }

    }

    public void dispose() {
    }
}
