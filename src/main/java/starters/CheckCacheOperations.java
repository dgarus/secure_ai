package starters;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.plugin.security.SecurityCredentials;
import org.apache.ignite.plugin.security.SecurityException;
import security.SecurityPluginProvider;

public class CheckCacheOperations {

    public static void main(String[] args) {
        Ignite ignite = Ignition.start(
            new IgniteConfiguration()
                .setPeerClassLoadingEnabled(true)
                .setPluginProviders(
                    new SecurityPluginProvider(new SecurityCredentials("secondSubject", null))
                )
        );
        try {
            ignite.cache("secret_cache").put("key", "value");
        }
        catch (SecurityException e) {
            System.out.println("The try to put to 'secret_cache': " + e.getMessage());
        }
        IgniteCache<String, String> cache = ignite.cache("common_cache");
        cache.put("key", "some_value");
        System.out.println("Cache " + cache.getName() + " key=" + cache.get("key"));
        try {
            cache.remove("key");
        }
        catch (SecurityException e) {
            System.out.println("The try to remove from 'common_cache': " + e.getMessage());
        }

        Ignition.stop(true);
    }
}
