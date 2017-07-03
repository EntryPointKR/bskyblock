package us.tastybento.bskyblock.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import us.tastybento.bskyblock.api.config.annotations.CheckDoubleInRange;
import us.tastybento.bskyblock.api.config.annotations.CheckIntInRange;
import us.tastybento.bskyblock.api.config.annotations.ConfigEntry;
import us.tastybento.bskyblock.database.BSBDatabase.DatabaseType;
import us.tastybento.bskyblock.database.managers.OfflineHistoryMessages.HistoryMessageType;

/**
 * All the plugin settings are here
 * @author Tastybento
 */
public class Settings {
    /* The settings variables order will define the config order. */

    // Constants
    // Game Type BSKYBLOCK or ACIDISLAND
    public enum GameType {
        BSKYBLOCK, ACIDISLAND
    }
    /*
    public final static GameType GAMETYPE = GameType.ACIDISLAND;
    // The spawn command (Essentials spawn for example)
    public final static String SPAWNCOMMAND = "spawn";
    // Permission prefix
    public final static String PERMPREFIX = "acidisland.";
    // The island command
    public final static String ISLANDCOMMAND = "ai";
    // The challenge command
    public static final String CHALLENGECOMMAND = "aic";
    // Admin command
    public static final String ADMINCOMMAND = "acid";
    */
    public final static GameType GAMETYPE = GameType.BSKYBLOCK;
    // Permission prefix
    public final static String PERMPREFIX = "bskyblock.";
    // The island command
    public final static String ISLANDCOMMAND = "island";
    // The challenge command
    public static final String CHALLENGECOMMAND = "bsc";
    // The spawn command (Essentials spawn for example)
    public final static String SPAWNCOMMAND = "spawn";
    // Admin command
    public static final String ADMINCOMMAND = "bsadmin";
    
    // --------------------------------------------------------------------
    
    /*      GENERAL     */
    @ConfigEntry(path = "general.metrics")
    public static boolean metrics = true;
    
    @ConfigEntry(path = "general.check-updates")
    public static boolean checkUpdates = true;
    
    @ConfigEntry(path = "general.default-langage")
    public static String defaultLanguage = "en-US";
    
    @ConfigEntry(path = "general.use-economy")
    public static boolean useEconomy = true;
    
    @ConfigEntry(path = "general.starting-money")
    @CheckDoubleInRange(min = 0.0, max = Double.MAX_VALUE)
    public static double startingMoney = 10.0;
    
    @ConfigEntry(path = "general.use-control-panel")
    public static boolean useControlPanel = true;
    
    // Purge
    @ConfigEntry(path = "general.purge.max-island-level")
    public static int purgeMaxIslandLevel = 50;
    
    @ConfigEntry(path = "general.purge.remove-user-data")
    public static boolean purgeRemoveUserData = false;
    
    // Database
    @ConfigEntry(path = "general.database.type")
    public static DatabaseType databaseType = DatabaseType.FLATFILE;
    
    @ConfigEntry(path = "general.database.settings.host")
    public static String dbHost = "localhost";
    
    @ConfigEntry(path = "general.database.settings.port")
    @CheckIntInRange(min = 1, max = 65535)
    public static int dbPort = 3306;
    
    @ConfigEntry(path = "general.database.settings.name")
    public static String dbName = "ASkyBlock";
    
    @ConfigEntry(path = "general.database.settings.username")
    public static String dbUsername = "username";
    
    @ConfigEntry(path = "general.database.settings.password")
    public static String dbPassword = "password";
    
    @ConfigEntry(path = "general.database.backup-period")
    @CheckIntInRange(min = 2, max = Integer.MAX_VALUE)
    public static int databaseBackupPeriod = 5;
    
    
    @ConfigEntry(path = "general.recover-super-flat")
    public static boolean recoverSuperFlat = false;
    
    @ConfigEntry(path = "general.mute-death-messages")
    public static boolean muteDeathMessages = false;
    
    @ConfigEntry(path = "general.FTB-auto-activator")
    public static boolean ftbAutoActivator = false;
    
    @ConfigEntry(path = "general.allow-obsidian-scooping")
    public static boolean allowObsidianScooping = true;
    
    // Teleport
    @ConfigEntry(path = "general.allow-teleport.falling")
    public static boolean fallingAllowTeleport = true;
    
    @ConfigEntry(path = "general.allow-teleport.falling-blocked-commands")
    public static List<String> fallingBlockedCommands = Arrays.asList("home");
    
    @ConfigEntry(path = "general.allow-teleport.acid")
    public static boolean acidAllowTeleport = true;
    
    @ConfigEntry(path = "general.allow-teleport.acid-blocked-commands")
    public static List<String> acidBlockedCommands = Arrays.asList("home");
    
    // --------------------------------------------------------------------
    
    /*      WORLD       */
    @ConfigEntry(path = "world.world-name")
    public static String worldName = "BSkyBlock";
    
    @ConfigEntry(path = "world.distance")
    @CheckIntInRange(min = 50, max = 1000)
    public static int islandDistance = 200;
    
    @ConfigEntry(path = "world.protection-range")
    @CheckIntInRange(min = 0, max = 1000)
    public static int islandProtectionRange = 100;
    
    @ConfigEntry(path = "world.start-x")
    public static int startX = 0;
    
    @ConfigEntry(path = "world.start-z")
    public static int startZ = 0;
    
    @ConfigEntry(path = "world.island-height")
    @CheckIntInRange(min = 5, max = 250)
    public static int islandHeight = 120;
    
    @ConfigEntry(path = "world.sea-height")
    @CheckIntInRange(min = 0, max = 250)
    public static int seaHeight = 0;
    
    @ConfigEntry(path = "world.max-islands")
    public static int maxIslands = 0;
    
    // Nether
    @ConfigEntry(path = "world.nether.generate")
    public static boolean netherGenerate = true;
    
    @ConfigEntry(path = "world.nether.islands")
    public static boolean netherIslands = true;
    
    @ConfigEntry(path = "world.nether.trees")
    public static boolean netherTrees = true;
    
    @ConfigEntry(path = "world.nether.roof")
    public static boolean netherRoof = true;
    
    @ConfigEntry(path = "world.nether.spawn-radius")
    @CheckIntInRange(min = 0, max = 100)
    public static int netherSpawnRadius = 25;
    
    //TODO End
    
    // Entities
    public static int spawnLimitMonsters;
    public static int spawnLimitAnimals;
    public static int spawnLimitWaterAnimals;
    public static HashMap<EntityType, Integer> entityLimits;
    public static HashMap<String, Integer> tileEntityLimits;
    
    @ConfigEntry(path = "world.disable-offline-redstone", experimental = true)
    public static boolean disableOfflineRedstone;
    
    /*      ISLAND      */
    @ConfigEntry(path = "island.default-max-team-size")
    @CheckIntInRange(min = 1, max = Integer.MAX_VALUE)
    public static int maxTeamSize = 4;
    
    @ConfigEntry(path = "island.default-max-homes")
    @CheckIntInRange(min = 1, max = Integer.MAX_VALUE)
    public static int maxHomes = 1;
    
    @ConfigEntry(path = "island.name.min-length")
    @CheckIntInRange(min = 0, max = Integer.MAX_VALUE)
    public static int nameMinLength = 5;
    
    @ConfigEntry(path = "island.name.max-length")
    @CheckIntInRange(min = 0, max = Integer.MAX_VALUE)
    public static int nameMaxLength = 20;
    
    @ConfigEntry(path = "island.invite-wait")
    public static int inviteWait = 60;
    
    // Reset
    @ConfigEntry(path = "island.reset.reset-limit")
    public static int resetLimit = -1;
    
    @ConfigEntry(path = "island.reset.reset-wait")
    public static int resetWait = 300;
    
    @ConfigEntry(path = "island.reset.leavers-lose-resets")
    public static boolean leaversLoseResets = true;
    
    @ConfigEntry(path = "island.reset.kicked-keep-inventory")
    public static boolean kickedKeepInventory = false;
    
    
    public static boolean onJoinResetMoney;
    public static boolean onJoinResetInventory;
    public static boolean onJoinResetEnderChest;
    public static boolean onLeaveResetMoney;
    public static boolean onLeaveResetInventory;
    public static boolean onLeaveResetEnderChest;
    
    // Remove mobs
    public static boolean removeMobsOnLogin;
    public static boolean removeMobsOnIsland;
    public static List<String> removeMobsWhitelist;
    
    public static boolean makeIslandIfNone;
    public static boolean immediateTeleportOnIsland;
    public static boolean respawnOnIsland;
    public static boolean onlyLeaderCanCoop;
    
    // Chats
    public static boolean teamchatUse;
    public static boolean teamchatLog;
    public static boolean teamchatIncludeTrust;
    public static boolean teamchatIncludeCoop;
    //TODO island chat
    
    public static boolean confirmKick;
    public static int confirmKickWait;
    public static boolean confirmLeave;
    public static int confirmLeaveWait;
    public static boolean confirmReset;
    public static int confirmResetWait;
    
    // Deaths
    public static int deathsMax;
    public static boolean deathsSumTeam;
    
    /*      PROTECTION      */
    public static boolean allowPistonPush;
    public static boolean restrictWither;
    
    // Invincible visitors
    public static boolean invincibleVisitor;
    public static List<DamageCause> invincibleVisitorOptions;
    
    public static List<String> visitorBannedCommands;
    public static int togglePvPCooldown;
    
    //TODO flags
    
    /*      ACID        */
    public static boolean acidDamageOp;
    public static boolean acidDamageChickens;
    
    // Damage
    public static int acidDamagePlayer;
    public static int acidDamageMonster;
    public static int acidDamageAnimal;
    public static int acidDestroyItemTime;
    public static int acidRainDamage;
    public static List<PotionEffectType> acidEffects;
    
    // Protection
    public static boolean acidHelmetProtection;
    public static boolean acidFullArmorProtection;
    
    /*      SCHEMATICS      */
    public static List<String> companionNames;
    public static ItemStack[] chestItems;
    public static Biome defaultBiome;
    public static boolean usePhysics;
    public static EntityType companionType;
    public static boolean useSchematicPanel;
    public static boolean chooseIslandRandomly;
    
    // TODO added this just to avoid compilation errors, but will be changed in the future
    public static List<HistoryMessageType> historyMessagesTypes;

    public static boolean useOwnGenerator;

    public static boolean createEnd;

    public static boolean islandEnd;
    public static boolean resetMoney;
    public static double acidDamage;
    public static int islandXOffset;
    public static int islandStartX;
    public static int islandZOffset;
    public static int islandStartZ;

}
