package dev.asjordi;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    @Test
    void test() {
        logger.atTrace().log("Running MainTest");
        assertTrue(true);
    }

}
