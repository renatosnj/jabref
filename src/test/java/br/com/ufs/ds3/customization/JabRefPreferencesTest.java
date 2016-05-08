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
    public void testImportacaoPreferencias() throws JabRefException {
        File importFile = new File("src/test/resources/net/sf/jabref/customPreferences.xml");

        preferencias.importPreferences(importFile.getAbsolutePath());

        String esperado = "emacsPath";
        String atual = JabRefPreferences.EMACS_PATH;
        assertEquals(esperado, atual);
    }

    @Test
    public void testPutBoolean() {
        preferencias.putBoolean(JabRefPreferences.BIB_LOC_AS_PRIMARY_DIR, true);
        assertEquals(preferencias.getBoolean(JabRefPreferences.BIB_LOC_AS_PRIMARY_DIR), true);
    }

    @Test
    public void testEditor() {
        assertEquals(JabRefPreferences.EMACS_23, "emacsUseV23InsertString");
    }

    @Test
    public void testArquivoImportPreferences() {
        boolean test = false;
        File file = new File("jabref.xml");
        try {
            JabRefPreferences.getInstance().importPreferences("jabref.xml");
            test = true;
        } catch (JabRefException e) {
            test = false;
            System.out.println("ERRO!");
        }
        assertEquals(true, test);
    }

    @Test
    public void testDefaltsPreferences() {
        String esperado = "Arial";
        JabRefPreferences.getInstance().defaults.put("FONT_FAMILY", "Arial");
        String atual = (String) JabRefPreferences.getInstance().defaults.get("FONT_FAMILY");
        assertEquals(esperado, atual);
    }
}
