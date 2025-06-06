package com.robot.game.attacks;

import com.badlogic.gdx.graphics.Texture;
import com.robot.game.constants.FilterBits;
import com.robot.game.enemies.Enemy;
import com.robot.game.enums.EnemyState;

public abstract class EnemyAttack extends Attack {
    public Enemy enemy;
    public EnemyAttack(float x, float y, float width, float height, Texture texture, float range, int damage, float duration, float angle) {
        super(x, y, width, height, texture, range, damage, duration, angle);
        initCategoryBits(FilterBits.ENEMY_ATTACK_BITS, (short) 0, (short) (FilterBits.OBSTACLE_BITS | FilterBits.PLAYER_BITS));
    }
    public EnemyAttack(float x, float y, float width, Texture texture, float range, int damage, float duration, float angle) {
        super(x, y, width, texture, range, damage, duration, angle);
        initCategoryBits(FilterBits.ENEMY_ATTACK_BITS, (short) 0, (short) (FilterBits.OBSTACLE_BITS | FilterBits.PLAYER_BITS));
    }
    public void reload(){
        enemy.setEnemyState(EnemyState.RELOADING);
    }
    @Override
    public void reSetBits(){
        super.reSetBits();
        initCategoryBits(FilterBits.ENEMY_ATTACK_BITS, (short) 0, (short) (FilterBits.PLAYER_BITS | FilterBits.OBSTACLE_BITS));
    }


}
