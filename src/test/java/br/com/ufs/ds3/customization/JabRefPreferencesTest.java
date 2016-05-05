package br.com.ufs.ds3.customization;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sf.jabref.JabRefException;
import net.sf.jabref.JabRefPreferences;

public class JabRefPreferencesTest {

    private JabRefPreferences preferencias;

    private JabRefPreferences backup;

    @Before
    public void setUp() throws Exception {
        preferencias = JabRefPreferences.getInstance();
        backup = preferencias;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws JabRefException {
        File importFile = new File("src/test/resources/net/sf/jabref/customPreferences.xml");

        preferencias.importPreferences(importFile.getAbsolutePath());

        String expected = "useProxy";
        String actual = preferencias.get(JabRefPreferences.PROXY_USE);

        assertEquals(expected, actual);
    }
}
