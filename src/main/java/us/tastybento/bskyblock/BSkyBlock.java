package us.tastybento.bskyblock;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import us.tastybento.bskyblock.commands.IslandCommand;
import us.tastybento.bskyblock.config.BSBLocale;
import us.tastybento.bskyblock.config.PluginConfig;
import us.tastybento.bskyblock.config.Settings;
import us.tastybento.bskyblock.database.BSBDatabase;
import us.tastybento.bskyblock.database.BSBDatabase.DatabaseType;
import us.tastybento.bskyblock.database.managers.IslandsManager;
import us.tastybento.bskyblock.database.managers.OfflineHistoryMessages;
import us.tastybento.bskyblock.database.managers.PlayersManager;
import us.tastybento.bskyblock.generators.IslandWorld;
import us.tastybento.bskyblock.schematics.SchematicsMgr;
import us.tastybento.bskyblock.util.FileLister;
import us.tastybento.bskyblock.util.VaultHelper;

/**
 * Main BSkyBlock class - provides an island minigame in the sky
 * @author Tastybento
 * @author Poslovitch
 */
public class BSkyBlock extends JavaPlugin{

    final static String LOCALE_FOLDER = "locales";
    
    private static BSkyBlock plugin;
    
    private HashMap<String, BSBLocale> locales = new HashMap<String, BSBLocale>();

    // Databases
    private PlayersManager playersManager;
    private IslandsManager islandsManager;
    private OfflineHistoryMessages offlineHistoryMessages;

    // Schematics
    private SchematicsMgr schematicsManager;

    // Metrics
    private Metrics metrics;

    @Override
    public void onEnable(){
        plugin = this;
        
        for(Field f : Settings.class.getDeclaredFields()){
            System.out.println(f.toString());

            for(Annotation a : f.getAnnotations()){
                System.out.println(a.toString());
            }
        }
        
        // Load configuration and locales. If there are no errors, load the plugin.
        if(PluginConfig.loadPluginConfig(this)){
            // TEMP DEBUG DATABASE
            /*
            Settings.databaseType = DatabaseType.MYSQL;
            Settings.dbHost = "localhost";
            Settings.dbPort = 3306;
            Settings.dbName = "ASkyBlock";
            Settings.dbUsername = "username";
            Settings.dbPassword = "password";
            */
            playersManager = new PlayersManager(this);
            islandsManager = new IslandsManager(this);
            // Only load metrics if set to true in config
            if(Settings.metrics) metrics = new Metrics(this);

            // If metrics are loaded, register the custom data charts
            if(metrics != null){
                registerCustomCharts();
            }

            offlineHistoryMessages = new OfflineHistoryMessages(this);
            offlineHistoryMessages.load();

            if (Settings.useEconomy && !VaultHelper.setupEconomy()) {
                getLogger().warning("Could not set up economy! - Running without an economy.");
                Settings.useEconomy = false;
            }

            VaultHelper.setupPermissions();

            // These items have to be loaded when the server has done 1 tick.
            // Note Worlds are not loaded this early, so any Locations or World reference will be null
            // at this point. Therefore, the 1 tick scheduler is required.
            getServer().getScheduler().runTask(this, new Runnable() {

                @Override
                public void run() {
                    // Create the world if it does not exist
                    // TODO: get world name from config.yml
                    Settings.worldName = "BSkyBlock_world";
                    Settings.createNether = true;
                    Settings.createEnd = true;
                    Settings.islandNether = false;
                    Settings.islandEnd = false;
                    new IslandWorld(plugin);

                    // Test: Create a random island and save it
                    // TODO: ideally this should be in a test class!
                    /*
                    UUID owner = UUID.fromString("ddf561c5-72b6-4ec6-a7ea-8b50a893beb2");

                    Island island = islandsManager.createIsland(new Location(getServer().getWorld("world"),0,0,0,0,0), owner);
                    // Add members
                    Set<UUID> randomSet = new HashSet<UUID>();
                    island.addMember(owner);
                    for (int i = 0; i < 10; i++) {
                        randomSet.add(UUID.randomUUID());
                        island.addMember(UUID.randomUUID());
                        island.addToBanList(UUID.randomUUID());
                    }
                    island.setBanned(randomSet);
                    island.setCoops(randomSet);
                    island.setTrustees(randomSet);
                    island.setMembers(randomSet);
                    for (SettingsFlag flag: SettingsFlag.values()) {
                        island.setFlag(flag, true);
                    }
                    island.setLocked(true);
                    island.setName("new name");
                    island.setPurgeProtected(true);
                    islandsManager.save(false);


                    getLogger().info("DEBUG: ************ Finished saving, now loading *************");

                     */

                    playersManager.load();
                    islandsManager.load();

                    // Load schematics
                    // TODO: load these from config.yml
                    Settings.chestItems = new ItemStack[] {
                            new ItemStack(Material.LAVA_BUCKET,1),
                            new ItemStack(Material.ICE,2),
                            new ItemStack(Material.MELON_SEEDS,1),
                            new ItemStack(Material.BONE,2),
                            new ItemStack(Material.COBBLESTONE,5),
                            new ItemStack(Material.SAPLING,2)
                    };
                    schematicsManager = new SchematicsMgr(plugin);

                    getCommand("island").setExecutor(new IslandCommand(plugin));

                    Settings.defaultLanguage = "en-US";
                    loadLocales();

                    /*
                     *DEBUG CODE
                    Island loadedIsland = islandsManager.getIsland(owner);
                    getLogger().info("Island name = " + loadedIsland.getName());
                    getLogger().info("Island locked = " + loadedIsland.getLocked());
                    //getLogger().info("Random set = " + randomSet);
                    getLogger().info("Island coops = " + loadedIsland.getCoops());
                    for (Entry<SettingsFlag, Boolean> flag: loadedIsland.getFlags().entrySet()) {
                        getLogger().info("Flag " + flag.getKey().name() + " = " + flag.getValue());
                    }
                     */
                    // Save islands & players data asynchronously every X minutes
                    Settings.databaseBackupPeriod = 10 * 60 * 20;
                    plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {

                        @Override
                        public void run() {
                            playersManager.save(true);
                            islandsManager.save(true);
                            offlineHistoryMessages.save(true);
                        }
                    }, Settings.databaseBackupPeriod, Settings.databaseBackupPeriod);
                }
                // TODO Auto-generated method stub

            });
        }
    }

    @Override
    public void onDisable(){
        // Save data
        playersManager.shutdown();
        islandsManager.shutdown();
        //offlineHistoryMessages.shutdown();
        plugin = null;
    }

    private void registerCustomCharts(){   
        metrics.addCustomChart(new Metrics.SingleLineChart("islands_count") {

            @Override
            public int getValue() {
                return islandsManager.getCount();
            }
        });

        metrics.addCustomChart(new Metrics.SingleLineChart("created_islands") {

            @Override
            public int getValue() {
                int created = islandsManager.metrics_getCreatedCount();
                islandsManager.metrics_setCreatedCount(0);
                return created;
            }
        });

        metrics.addCustomChart(new Metrics.SimplePie("default_locale") {

            @Override
            public String getValue() {
                return Settings.defaultLanguage;
            }
        });

        metrics.addCustomChart(new Metrics.SimplePie("database") {

            @Override
            public String getValue() {
                return BSBDatabase.getDatabase().toString();
            }
        });
    }

    /**
     * Returns an HashMap of locale identifier and the related object
     * @return the locales
     */
    public HashMap<String, BSBLocale> getLocales(){
        return locales;
    }

    /**
     * Set the available locales
     * @param locales - the locales to set
     */
    public void setLocales(HashMap<String, BSBLocale> locales){
        this.locales = locales;
    }

    /**
     * Returns the default locale
     * @return the default locale
     */
    public BSBLocale getLocale(){
        return locales.get(Settings.defaultLanguage);
    }

    /**
     * Returns the locale for the specified CommandSender
     * @param sender - CommandSender to get the locale
     * @return if sender is a player, the player's locale, otherwise the default locale
     */
    public BSBLocale getLocale(CommandSender sender){
        if(sender instanceof Player) return getLocale(((Player) sender).getUniqueId());
        else return getLocale();
    }

    /**
     * Returns the locale for the specified player
     * @param player - Player to get the locale
     * @return the locale for this player
     */
    public BSBLocale getLocale(UUID player){
        getLogger().info("DEBUG: " + player);
        getLogger().info("DEBUG: " + getPlayers() == null ? "Players is null":"Players in not null");
        getLogger().info("DEBUG: " + getPlayers().getPlayer(player));
        getLogger().info("DEBUG: " + getPlayers().getPlayer(player).getLocale());
        String locale = getPlayers().getPlayer(player).getLocale();
        if(locale.isEmpty() || !locales.containsKey(locale)) return locales.get(Settings.defaultLanguage);

        return locales.get(locale);
    }

    /**
     * Loads all the locales available. If the locale folder does not exist, one will be created and
     * filled with locale files from the jar.
     */
    public void loadLocales() {
        // Describe the filter - we only want files that are correctly named
        FilenameFilter ymlFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                //plugin.getLogger().info("DEBUG: filename = " + name);
                if (name.toLowerCase().startsWith("bsb_") && name.toLowerCase().endsWith(".yml")) {
                    // See if this is a valid locale
                    //Locale localeObject = new Locale(name.substring(0, 2), name.substring(3, 5));
                    Locale localeObject = Locale.forLanguageTag(name.substring(4, name.length() - 4));
                    if (localeObject == null) {
                        plugin.getLogger().severe("Filename '" + name + "' is an unknown locale, skipping...");
                        return false;
                    }
                    return true;
                } else {
                    if (name.toLowerCase().endsWith(".yml")) {
                        plugin.getLogger().severe("Filename '" + name + "' is not in the correct format for a locale file - skipping...");
                    }
                    return false;
                }
            }
        };
        // Run through the files and store the locales
        File localeDir = new File(this.getDataFolder(), LOCALE_FOLDER);
        // If the folder does not exist, then make it and fill with the locale files from the jar
        if (!localeDir.exists()) {
            localeDir.mkdir();
            FileLister lister = new FileLister(this);
            try {
                for (String name : lister.listJar(LOCALE_FOLDER)) {
                    this.saveResource(name,true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Store all the locales available
        for (String language : localeDir.list(ymlFilter)) {  
            try {
                BSBLocale locale = new BSBLocale(this, language);
                locales.put(locale.getLocaleId(), locale);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Returns the player database
     * @return the player database
     */
    public PlayersManager getPlayers(){
        return playersManager;
    }

    /**
     * Returns the island database
     * @return the island database
     */
    public IslandsManager getIslands(){
        return islandsManager;
    }

    public static BSkyBlock getPlugin() {
        return plugin;
    }

    /**
     * @return the schematics
     */
    public SchematicsMgr getSchematics() {
        return schematicsManager;
    }

}
