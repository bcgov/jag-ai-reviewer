package ca.bc.gov.open.jag.aireviewerapi.util;

import ca.bc.gov.open.jag.aireviewerapi.utils.MD5;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MD5Test {

    private static final String EXPECTED_HASH = "098f6bcd4621d373cade4e832627b4f6";

    @Test()
    @DisplayName("Success: value hashed")
    public void testMD5Success() {

        Assertions.assertEquals(EXPECTED_HASH, MD5.hashValue("test"));

    }

}
