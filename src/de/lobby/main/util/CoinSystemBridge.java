package de.lobby.main.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class CoinSystemBridge {

    private final Object coinApi;
    private final Method addCoinsMethod;

    public CoinSystemBridge() {
        Plugin coinSystem = Bukkit.getPluginManager().getPlugin("CoinSystem");
        if (coinSystem == null || !coinSystem.isEnabled()) {
            throw new IllegalStateException("CoinSystem ist nicht aktiviert.");
        }

        try {
            Method getApiMethod = coinSystem.getClass().getMethod("getCoinSystemAPI");
            coinApi = getApiMethod.invoke(coinSystem);
            if (coinApi == null) {
                throw new IllegalStateException("CoinSystemAPI ist nicht registriert.");
            }
            addCoinsMethod = coinApi.getClass().getMethod(
                    "addCoins", UUID.class, String.class, double.class
            );
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("CoinSystemAPI konnte nicht geladen werden.", exception);
        }
    }

    @SuppressWarnings("unchecked")
    public CompletableFuture<Double> addCoins(UUID playerId, String playerName, double amount) {
        try {
            return (CompletableFuture<Double>) addCoinsMethod.invoke(
                    coinApi, playerId, playerName, amount
            );
        } catch (IllegalAccessException exception) {
            return failedFuture(exception);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause() == null ? exception : exception.getCause();
            return failedFuture(cause);
        }
    }

    private static CompletableFuture<Double> failedFuture(Throwable error) {
        CompletableFuture<Double> future = new CompletableFuture<>();
        future.completeExceptionally(error);
        return future;
    }
}
