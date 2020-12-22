package starters;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.plugin.security.SecurityCredentials;
import security.SecurityPluginProvider;

public class SandboxTest {
    public static void main(String[] args) {
        Ignite ignite = Ignition.start(
            new IgniteConfiguration()
                .setPeerClassLoadingEnabled(true)
                .setPluginProviders(
                    new SecurityPluginProvider(new SecurityCredentials("sandboxSubject", null))
                )
        );
        try {
            IgniteCompute compute = ignite.compute(ignite.cluster().forRemotes());
            System.out.println("Java version: " + compute.call(new PropertyReader("java.version")));

            System.out.println("Java home: " + compute.call(new PropertyReader("java.home")));
        }
        finally {
            Ignition.stop(true);
        }
    }

    private static class PropertyReader implements IgniteCallable<String>{
        private final String propertyName;

        public PropertyReader(String propertyName) {
            this.propertyName = propertyName;
        }

        @Override public String call() {
            return System.getProperty(propertyName);
        }
    }
}
