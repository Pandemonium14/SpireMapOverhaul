package spireMapOverhaul.zones.glassblower.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.glassblower.GlassblowerZone;
import spireMapOverhaul.zones.glassblower.monsters.GlassGolem;
import sun.security.krb5.internal.crypto.Des;

public class GlassProtectPower extends AbstractSMOPower {

    public static final String POWER_ID = SpireAnniversary6Mod.makeID("GlassProtectPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private AbstractMonster protector;

    public GlassProtectPower(AbstractCreature owner, AbstractMonster protector) {
        super(POWER_ID, NAME, GlassblowerZone.ID , PowerType.BUFF, false, owner, 1);
        this.protector = protector;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        if (protector != null) {
            description = DESCRIPTIONS[0] + protector.name + DESCRIPTIONS[1];
        }
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.output > owner.currentBlock) {
            addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            DamageInfo newInfo = new DamageInfo(info.owner, info.base - owner.currentBlock, info.type);
            addToTop(new DamageAction(protector, newInfo));
        }
        return 0;
    }


}
