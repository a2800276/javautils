package util.json;


import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DynamicEncoderTest {
    static final String HOUSE_JSON = "{'house': {'number_of_windows':1, 'roof': 'yes'}}";

    @Test
    public static void testHouse() {
        String json = JSON.jsonifyDynamic(new House());
        assertEquals(json, HOUSE_JSON);
    }


    static class House {
        int numWindows;
        boolean roof;

        public String toJSON() {
            return HOUSE_JSON;
        }
    }
}
