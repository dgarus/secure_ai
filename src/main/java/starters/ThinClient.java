package starters;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

public class ThinClient {
    public static void main(String[] args) throws Exception {
        ClientConfiguration config = new ClientConfiguration()
            .setAddresses("127.0.0.1:10800")
            .setUserName("secondSubject")
            .setUserPassword("");

        try (IgniteClient client = Ignition.startClient(config)) {
            ClientCache<String, String> cache = client.cache("common_cache");
            cache.put("key", "some_value");
            System.out.println("Cache " + cache.getName() + " key=" + cache.get("key"));
            cache.remove("key");
        }
    }
}
