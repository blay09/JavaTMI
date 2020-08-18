package net.blay09.javatmi;

import net.blay09.javairc.IRCMessage;
import net.blay09.javairc.IRCUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TwitchUser {

    private final IRCUser user;
    private String[] badges;
    private List<TwitchEmote> emotes;
    private String color;
    private String displayName;
    private int userId;
    private UserType userType;
    private boolean mod;
    private boolean subscriber;
    private int subscribedMonths = 0;
    private int cheeredBits = 0;
    private boolean turbo;

    public TwitchUser(IRCUser user) {
        this.user = user;
    }

    public boolean hasColor() {
        return color != null && !color.isEmpty();
    }

    public boolean hasEmotes() {
        return emotes != null;
    }

    public boolean hasBadges() {
        return badges != null;
    }

    public String getDisplayName() {
        return displayName != null && !displayName.isEmpty() ? displayName : user.getNick();
    }

    public String getNick() {
        return user.getNick();
    }

    public static TwitchUser fromMessage(IRCMessage message) {
        return parseMessageTags(new TwitchUser(message.parseSender()), message);
    }

    public static TwitchUser fromMessageTags(IRCMessage message) {
        return fromMessageTags(message, message.getTagByKey("login"));
    }

    public static TwitchUser fromMessageTags(IRCMessage message, String nick) {
        return parseMessageTags(new TwitchUser(new IRCUser(nick, null, null)), message);
    }

    private static TwitchUser parseMessageTags(TwitchUser twitchUser, IRCMessage message) {
        String badgesTag = message.getTagByKey("badges");
        if(badgesTag != null) {
            twitchUser.badges = badgesTag.split(",");
            for (String s: twitchUser.badges) {
                if (s.startsWith("subscriber")) {
                    twitchUser.subscribedMonths = Integer.parseInt(s.split("/")[1]);
                } else if(s.startsWith("bits")) {
                    twitchUser.cheeredBits = Integer.parseInt(s.split("/")[1]);
                }
            }
        }
        String emotesTag = message.getTagByKey("emotes");
        if(emotesTag != null) {
            String[] emotes = emotesTag.split("/");
            for (String emoteData : emotes) {
                if (twitchUser.emotes == null) {
                    twitchUser.emotes = new ArrayList<>();
                }
                int colonIdx = emoteData.indexOf(':');
                if (colonIdx != -1) {
                    String emoteId = emoteData.substring(0, colonIdx);
                    String[] occurrences = emoteData.substring(colonIdx + 1).split(",");
                    for (String occurrenceData : occurrences) {
                        int dashIdx = occurrenceData.indexOf('-');
                        if (dashIdx != -1) {
                            int start = Integer.parseInt(occurrenceData.substring(0, dashIdx));
                            int end = Integer.parseInt(occurrenceData.substring(dashIdx + 1));
                            twitchUser.emotes.add(new TwitchEmote(emoteId, start, end));
                        }
                    }
                }
            }
        }
        twitchUser.color = message.getTagByKey("color");
        twitchUser.displayName = message.getTagByKey("display-name");
        twitchUser.mod = Objects.equals(message.getTagByKey("mod"), "1");
        twitchUser.subscriber = Objects.equals(message.getTagByKey("subscriber"), "1");
        twitchUser.turbo = Objects.equals(message.getTagByKey("turbo"), "1");
        try {
            twitchUser.userId = Integer.parseInt(message.getTagByKey("user-id"));
        } catch(NumberFormatException ignored) {
        }
        twitchUser.userType = UserType.fromTag(message.getTagByKey("user-type"));
        return twitchUser;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isSubscriber() {
        return subscriber;
    }

    public void setSubscriber(boolean subscriber) {
        this.subscriber = subscriber;
    }

    public int getSubscribedMonths() {
        return subscribedMonths;
    }

    public void setSubscribedMonths(int subscribedMonths) {
        this.subscribedMonths = subscribedMonths;
    }

    public boolean isTurbo() {
        return turbo;
    }

    public boolean isMod() {
        return mod;
    }

    public void setMod(boolean mod) {
        this.mod = mod;
    }

    public int getCheeredBits() {
        return cheeredBits;
    }

    public void setCheeredBits(int cheeredBits) {
        this.cheeredBits = cheeredBits;
    }

    public void setTurbo(boolean turbo) {
        this.turbo = turbo;
    }

    public List<TwitchEmote> getEmotes() {
        return emotes;
    }

    public String[] getBadges() {
        return badges;
    }

    public void setBadges(String[] badges) {
        this.badges = badges;
    }
}
