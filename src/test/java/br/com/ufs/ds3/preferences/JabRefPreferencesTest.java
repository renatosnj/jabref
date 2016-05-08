package br.com.ufs.ds3.preferences;

import static org.junit.Assert.assertEquals;

import net.sf.jabref.JabRefPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JabRefPreferencesTest {

    private JabRefPreferences prefs;

    private JabRefPreferences backup;


    @Before
    public void setUp() {
        prefs = JabRefPreferences.getInstance();
        backup = prefs;
    }

    @Test
    public void testPutBooleanBibLoc() {
        prefs.putBoolean(JabRefPreferences.BIB_LOC_AS_PRIMARY_DIR, true);
        assertEquals(prefs.getBoolean(JabRefPreferences.BIB_LOC_AS_PRIMARY_DIR), true);

        prefs.putBoolean(JabRefPreferences.BIB_LOC_AS_PRIMARY_DIR, true);
        assertEquals(prefs.getBoolean(JabRefPreferences.BIB_LOC_AS_PRIMARY_DIR), true);
    }

    @Test
    public void testPutBooleanAllowFile() {
        prefs.putBoolean(JabRefPreferences.ALLOW_FILE_AUTO_OPEN_BROWSE, true);
        assertEquals(prefs.getBoolean(JabRefPreferences.ALLOW_FILE_AUTO_OPEN_BROWSE), true);

        prefs.putBoolean(JabRefPreferences.ALLOW_FILE_AUTO_OPEN_BROWSE, false);
        assertEquals(prefs.getBoolean(JabRefPreferences.ALLOW_FILE_AUTO_OPEN_BROWSE), false);
    }

    @After
    public void tearDown() {
        //clean up preferences to default state
        prefs.overwritePreferences(backup);
    }

}
