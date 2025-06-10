package me.sfiguz7.transcendence.implementation.tasks;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.armor.RadiationTask;
import io.github.thebusybiscuit.slimefun4.utils.RadiationUtils;
import me.sfiguz7.transcendence.TranscEndence;
import me.sfiguz7.transcendence.implementation.items.items.Daxi;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Custom radiation handling task that integrates with Daxi radiation protection system.
 * This class extends RadiationTask and overrides onPlayerTick to check for Daxi radiation protection.
 */
public class DaxiRadiationTask extends RadiationTask {

    private static final String DEBUG_PREFIX = "[DaxiRadiationTask] ";
    private final boolean debugEnabled;    /**
     * Constructor for DaxiRadiationTask
     */
    public DaxiRadiationTask() {
        // Call parent constructor without parameters
        super();
        // Cache debug setting to avoid repeated config lookups
        this.debugEnabled = TranscEndence.getInstance().getConfig().getBoolean("debug.radiation", false);
    }

    /**
     * Override the onPlayerTick method to integrate with Daxi system.
     * This method completely replaces the original radiation logic to avoid conflicts.
     * 
     * @param p The player to check for radiation protection
     * @param profile The player's profile
     */    @Override
    protected void onPlayerTick(Player p, PlayerProfile profile) {
        // Safety checks
        if (p == null || !p.isOnline() || !p.isValid() || p.isDead()) {
            return;
        }
        
        if (profile == null) {
            if (debugEnabled) {
                logDebug("PlayerProfile is null for " + p.getName() + " - skipping radiation check");
            }
            return;
        }
        
        String taskId = this.toString(); // Unique task identifier
        
        if (debugEnabled) {
            logDebug("TASK[" + taskId + "] Checking radiation for player: " + p.getName());
        }
        
        // Debug: Check immunity status multiple times in same tick
        boolean immunity1 = Daxi.hasCustomEffect(p, Daxi.DaxiEffectType.RADIATION_IMMUNITY);
        boolean immunity2 = Daxi.hasCustomEffect(p, Daxi.DaxiEffectType.RADIATION_IMMUNITY);


        if (debugEnabled) {
            logDebug("TASK[" + taskId + "] Immunity check: " + immunity1 + "/" + immunity2);
        }

        // Alert if checks are inconsistent
        if (immunity1 != immunity2 ) {
            logDebug("WARNING: Inconsistent protection checks for " + p.getName() + 
                    " - Immunity: " + immunity1 + "/" + immunity2);
        }

        // Check if player has Daxi radiation immunity
        if (immunity1) { // Use first check
            if (debugEnabled) {
                logDebug("TASK[" + taskId + "] Player " + p.getName() + 
                        " has Daxi RADIATION_IMMUNITY - maintaining zero exposure");
            }
            
            // Clear radiation and return - NO super() call
            try {
                int currentExposure = RadiationUtils.getExposure(p);
                if (currentExposure > 0) {
                    RadiationUtils.removeExposure(p, currentExposure);
                }
            } catch (Exception e) {
                if (debugEnabled) {
                    logDebug("Error clearing radiation for " + p.getName() + ": " + e.getMessage());
                }
            }
            
            if (debugEnabled) {
                logDebug("TASK[" + taskId + "] Immunity path completed - RETURNING without super()");
            }
            return; // CRITICAL: This should prevent super() call
        }
    
        
        if (debugEnabled) {
            logDebug("TASK[" + taskId + "] No Daxi protection - CALLING super().onPlayerTick()");
        }
        
        // If no Daxi protection, use original radiation logic
        try {
            super.onPlayerTick(p, profile);
        } catch (Exception e) {
            if (debugEnabled) {
                logDebug("Error in super.onPlayerTick for " + p.getName() + ": " + e.getMessage());
            }
        }
        
        if (debugEnabled) {
            logDebug("TASK[" + taskId + "] super().onPlayerTick() completed");
        }
    }
    /**
     * Logs debug messages if radiation debugging is enabled in config
     *
     * @param message The debug message to log
     */
    private void logDebug(String message) {
        TranscEndence.getInstance().getLogger().log(Level.INFO, DEBUG_PREFIX + message);
    }
}