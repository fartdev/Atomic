package me.zeroX150.atomic.helper;

import net.minecraft.entity.LivingEntity;

public class AttackManager {
    public static final long MAX_ATTACK_TIMEOUT = 5000;
    static long lastAttack = 0;
    static LivingEntity lastAttacked;

    public static LivingEntity getLastAttackInTimeRange() {
        if (lastAttack + MAX_ATTACK_TIMEOUT < System.currentTimeMillis()) {
            lastAttacked = null;
        }
        return lastAttacked;
    }

    public static void registerLastAttacked(LivingEntity entity) {
        lastAttacked = entity;
        lastAttack = System.currentTimeMillis();
    }

    public static long getLastAttack() {
        return lastAttack;
    }
}
