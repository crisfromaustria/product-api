package at.fontain.liveness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LivenessApp {
    private static final Logger log = LoggerFactory.getLogger(LivenessApp.class);

    public static void main(String[] args) {
        SpringApplication.run(LivenessApp.class, args);

/*
   _ _ __         _      __ _ _
  / / / / ___ _ _(_) ___ \ \ \ \
 / / / / / __| '_| |/ __| \ \ \ \
( ( ( (  ||__| | | |\__ \  ) ) ) )
 \ \ \ \ \___|_| |_||___/ / / / /
  \_\_\_\================/_/_/_/
*/

        log.info("   _ _ __         _      __ _ _");
        log.info("  / / / / ___ _ _(_) ___ \\ \\ \\ \\");
        log.info(" / / / / / __| '_| |/ __| \\ \\ \\ \\");
        log.info("( ( ( (  ||__| | | |\\__ \\  ) ) ) )");
        log.info(" \\ \\ \\ \\ \\___|_| |_||___/ / / / /");
        log.info("  \\_\\_\\_\\================/_/_/_/");
    }
}
