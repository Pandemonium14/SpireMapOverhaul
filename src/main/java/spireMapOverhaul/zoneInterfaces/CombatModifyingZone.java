package spireMapOverhaul.zoneInterfaces;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public interface CombatModifyingZone {
    /*
     * These methods mirror their relic counterparts and generally happen after them.
     * Unless specified, they refer to the player.
     * For more complex modifications, apply a custom power that explains itself to the player.
     */

    default void atPreBattle() {}

    default void atBattleStartPreDraw() {}

    default void atBattleStart() {}

    default void atTurnStart() {}

    default void atTurnStartPostDraw() {}

    default void atTurnEnd() {}

    default void atRoundEnd() {}

    default void onVictory() {}

    default void onMonsterDeath(AbstractMonster monster) {}

    /**
     * Returns a String that will be displayed when hovering a button during combat to explain modifications to it.
     * Should be used if the effect isn't immediately noticeable during combat start.
     * @return The string the button will display when hovered. This is decided at combat start and not dynamic.
     */
    default String getCombatText() {return null;}
}
