package com.argo_entertainment.reactiontime;

import com.badlogic.gdx.math.Vector2;

public interface ActionResolver {
    public boolean getSignedInGPGS();
    public void loginGPGS();
    public void silentLoginGPGS();
    public void showLeaderboard();
    public void showPop();
    public void submitScoreGPGS(int score);
    public void buyCoins(int coins);
    public void unlockAchievementGPGS(String achievementId);
    public void getLeaderboardGPGS();
    public void getAchievementsGPGS();
    public Vector2 getSceenOffset();
}
