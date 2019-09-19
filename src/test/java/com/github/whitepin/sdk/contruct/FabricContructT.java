package com.github.whitepin.sdk.contruct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FabricContructT {
    private FabricContruct fabricContruct;

    @BeforeEach
    public void setUp() {
        fabricContruct = new FabricContruct();
    }

    @Test
    public void setUpTest() throws Exception {
        fabricContruct.setUp();
    }
}
