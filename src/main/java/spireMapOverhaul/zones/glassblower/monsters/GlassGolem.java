package spireMapOverhaul.zones.glassblower.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOMonster;
import spireMapOverhaul.zones.glassblower.actions.StealCardAction;
import spireMapOverhaul.zones.glassblower.powers.GlassProtectPower;
import spireMapOverhaul.zones.invasion.powers.DrawReductionSingleTurnPower;

import java.util.ArrayList;
import java.util.Iterator;

public class GlassGolem extends AbstractSMOMonster {

    public static final String ID = SpireAnniversary6Mod.makeID("GlassGolem");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;

    //moves
    private static final byte WAIT = 1;
    private static final byte REVIVE = 2;
    private static final byte STEAL = 3;
    private static final byte PROTECT = 4;

    private static final boolean canDie = false;

    public GlassGolem() {
        super(NAME, ID, 10, 0, 0, 100, 100, null);
        loadAnimation(SpireAnniversary6Mod.makeImagePath("monsters/GlassGolem/skeleton.atlas"),
                SpireAnniversary6Mod.makeImagePath("monsters/GlassGolem/skeleton.json"), 1.5f);
        type = EnemyType.NORMAL;
        setHp(10);
        state.setAnimation(0,"Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new MinionPower(this)));
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case REVIVE:
                addToBot(new HealAction(this, this, this.maxHealth));
                halfDead = false;
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onSpawnMonster(this);// 125
                }
                break;
            case STEAL:
                addToBot(new FastShakeAction(this, 0.3F, 0.3F));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionSingleTurnPower(AbstractDungeon.player, 1)));
                addToBot(new StealCardAction(this));
                break;
            case PROTECT:
                GlassGolem protector = this;
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        ArrayList<AbstractMonster> monsters = new ArrayList<>();
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                            if (!(m instanceof GlassGolem) && !m.isDeadOrEscaped()) {
                                monsters.add(m);
                            }
                        }
                        int r = AbstractDungeon.aiRng.random(monsters.size() - 1);
                        addToTop(new ApplyPowerAction(monsters.get(r), protector, new GlassProtectPower(monsters.get(r), protector)));
                        isDone = true;
                    }
                });
                break;

            default: {}
        }
        firstMove = false;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if (halfDead) {
            setMove(REVIVE, Intent.BUFF);
        } else {
            if (lastMove(STEAL)) {
                setMove(PROTECT, Intent.BUFF);
            } else{
                setMove(STEAL, Intent.DEBUFF);
            }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);// 198
        if (this.currentHealth <= 0 && !halfDead) {
            this.halfDead = true;
            for (AbstractPower p : powers) {
                p.onDeath();
            }

            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }

            powers.clear();

            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m.hasPower(GlassProtectPower.POWER_ID)) {
                    addToTop(new RemoveSpecificPowerAction(m,m, m.getPower(GlassProtectPower.POWER_ID)));
                }
            }

            if (this.nextMove != 4) {
                this.setMove(WAIT, Intent.SLEEP);
                this.createIntent();
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, REVIVE, Intent.BUFF));
            }
        } else if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    @Override
    public void die() {
        if (canDie) {
            super.die();
        }
    }

    public void onMonsterDeath() {
        boolean isAlone = true;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!(m instanceof GlassGolem) && !m.isDeadOrEscaped()) {
                isAlone = false;
            }
        }
        if (isAlone) {
            escape();
            escapeTimer = 2.0f;
        }
    }

    @Override
    public void update() {
        super.update();
    }
}
