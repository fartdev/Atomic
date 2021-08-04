package me.zeroX150.atomic.helper;

import me.zeroX150.atomic.Atomic;
import net.minecraft.entity.LivingEntity;

public class AttackManager {
    public static final long MAX_ATTACK_TIMEOUT = 7000;
    static long lastAttack = 0;
    static LivingEntity lastAttacked;

    public static LivingEntity getLastAttackInTimeRange() {
        if (lastAttack + MAX_ATTACK_TIMEOUT < System.currentTimeMillis() || Atomic.client.player == null || Atomic.client.player.isDead())
            lastAttacked = null;
        if (lastAttacked != null) {
            if (lastAttacked.getPos().distanceTo(Atomic.client.player.getPos()) > 64 || lastAttacked.isDead())
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
