package me.zeroX150.atomic.mixin.network;

import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Session.class)
public class SessionMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(String username, String uuid, String accessToken, String accountType, CallbackInfo ci) {
        System.out.println("[D] Session was created!");
        System.out.println("username = " + username);
        System.out.println("uuid = " + uuid);
        System.out.println("accessToken = " + accessToken);
        System.out.println("accountType = " + accountType);
    }
}
