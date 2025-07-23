package com.prajwal.chatapp.service;

import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionService {
    private final Map<String, Gateway> UserToGateway = new ConcurrentHashMap<>();
    private final Set<String> OnlineUsers = ConcurrentHashMap.newKeySet();

    private static SessionService instance;

    private SessionService() {}

    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    public void registerUserToGateway(String userId, Gateway gateway) {
        UserToGateway.put(userId, gateway);
        OnlineUsers.add(userId);
    }

    public void registerLogout(String userId) {
        OnlineUsers.remove(userId);
        UserToGateway.remove(userId);
    }

    public Gateway getGateway(String userId) {
        return UserToGateway.get(userId);
    }

    public boolean isOnlineUser(String userId) {
        return OnlineUsers.contains(userId);
    }

    public boolean sendMessageToUser(String targetUserId, String message) {
        if (isOnlineUser(targetUserId)) {
            Gateway gateway = getGateway(targetUserId);
            if (gateway != null) {
                return gateway.sendMessageToUser(targetUserId, message);
            }
        }
        return false;
    }

    public void removeOnlineUser(String userId) {
        OnlineUsers.remove(userId);
    }
}
