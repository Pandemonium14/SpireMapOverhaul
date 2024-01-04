package spireMapOverhaul.zones.glassblower.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StasisPower;

public class StasisIdOffsetPower extends StasisPower {


    public StasisIdOffsetPower(AbstractCreature owner, AbstractCard card) {
        super(owner, card);
        ID += card.uuid;
    }
}
