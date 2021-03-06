package net.blay09.javatmi;

public interface TMIListener {
    void onChatMessage(TMIClient client, String channel, TwitchUser user, TwitchMessage message);
    void onWhisperMessage(TMIClient client, TwitchUser user, String message);
    void onServerMessage(TMIClient client, String channel, String messageId, String message);

    void onConnected(TMIClient client);
    void onDisconnected(TMIClient client);

    @Deprecated
    default void onSubscribe(TMIClient client, String channel, String username, boolean prime) {}
    default void onSubscribe(TMIClient client, String channel, TwitchUser user, SubscriptionInfo subscriptionInfo) {
        onSubscribe(client, channel, user.getNick(), "Prime".equals(subscriptionInfo.getSubPlan()));
    }

    void onGiftSubscription(TMIClient client, String channel, TwitchUser user, GiftSubscriptionInfo giftSubscriptionInfo);
    void onGiftPaidUpgrade(TMIClient client, String channel, TwitchUser user, GiftPaidUpgradeInfo giftPaidUpgradeInfo);

    @Deprecated
    default void onResubscribe(TMIClient client, String channel, TwitchUser user, int months, String message) {}
    default void onResubscribe(TMIClient client, String channel, TwitchUser user, SubscriptionInfo subscriptionInfo) {
        onResubscribe(client, channel, user, subscriptionInfo.getCumulativeMonths(), subscriptionInfo.getMessage());
    }

    void onHost(TMIClient client, String channel, String username, int viewers);
    void onUnhost(TMIClient client, String channel, int viewers);

    void onTimeout(TMIClient client, String channel, String username);
    void onClearChat(TMIClient client, String channel);

    void onUserState(TMIClient client, String channel, TwitchUser user);

    void onEmoteOnly(TMIClient client, String channel, boolean enabled);
    void onR9kBeta(TMIClient client, String channel, boolean enabled);
    void onSlowMode(TMIClient client, String channel, boolean enabled, int seconds);
    void onSubMode(TMIClient client, String channel, boolean enabled);

    void onHosted(TMIClient client, String channel, String username, int viewers);

	/**
     * Twitch IRC processes occasionally need to be restarted. When this happens, this method will be called. After a short period of time, the connection will be closed.
     * @param client the client backing up this connection
     */
    void onReconnectInbound(TMIClient client);

    void onUnhandledException(TMIClient client, Exception e);
}
