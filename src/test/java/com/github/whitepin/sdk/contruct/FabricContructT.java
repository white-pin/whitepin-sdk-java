package com.github.whitepin.sdk.contruct;

import org.junit.Before;
import org.junit.Test;

public class FabricContructT {
    private FabricContruct fabricContruct;

    @Before
    public void setUp() {
        fabricContruct = new FabricContruct();
    }

    @Test
    public void setUpTest() throws Exception {
        fabricContruct.setUp();
    }
}
