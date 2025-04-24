package dev.asjordi;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        WhoisService service = new WhoisService();
        String domain = "jordi.lat";

        try {
            var whois = service.performWhoisQuery(domain);
            if (whois.isPresent()) {
                System.out.println("WHOIS response for " + domain + ":");
                System.out.println(whois.get());
            } else {
                System.out.println("No WHOIS response available for " + domain);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
