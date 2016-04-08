package br.com.ufs.ds3.mail;

import java.net.URL;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.MetaData;
import net.sf.jabref.gui.BasePanel;
import net.sf.jabref.gui.JabRefFrame;
import net.sf.jabref.gui.worker.SendAsEMailAction;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.BibtexEntryTypes;

public class SendAsEmailWithOpenFoldersTest {

    private static boolean OPEN_FOLDERS_OF_ATTACHED_FILES;

    @BeforeClass
    public static void setUpBeforeClass() {
        Globals.prefs = JabRefPreferences.getInstance();
        OPEN_FOLDERS_OF_ATTACHED_FILES = JabRefPreferences.getInstance()
                .getBoolean(JabRefPreferences.OPEN_FOLDERS_OF_ATTACHED_FILES);
        JabRefPreferences.getInstance().putBoolean(JabRefPreferences.OPEN_FOLDERS_OF_ATTACHED_FILES, true);
    }

    @AfterClass
    public static void teardownAfterClass() {
        JabRefPreferences.getInstance().putBoolean(JabRefPreferences.OPEN_FOLDERS_OF_ATTACHED_FILES,
                OPEN_FOLDERS_OF_ATTACHED_FILES);
    }

    @Test
    public void generateCorrectUriMailToWithFileLinkAndOpenFolders() {
        JabRefFrame frame = Mockito.mock(JabRefFrame.class);
        BasePanel basePanel = Mockito.mock(BasePanel.class);
        MetaData metaData = Mockito.mock(MetaData.class);
        Mockito.when(frame.getCurrentBasePanel()).thenReturn(basePanel);

        URL url = getClass().getResource("/br/com/ufs/ds3/files/example.pdf");
        BibEntry[] selectedEntries = new BibEntry[] {new BibEntry("00000001", BibtexEntryTypes.ARTICLE)};
        selectedEntries[0].setField("file", ":" + url.getPath() + ":PDF");
        selectedEntries[0].setField("author", "Test Author");

        Mockito.when(basePanel.getSelectedEntries()).thenReturn(selectedEntries);
        Mockito.when(basePanel.metaData()).thenReturn(metaData);
        Mockito.when(metaData.getFileDirectory(Globals.FILE_FIELD)).thenReturn(new String[] {"/tmp"});

        SendAsEMailAction sendAsEMailAction = new SendAsEMailAction(frame);
        Assert.assertNotNull(sendAsEMailAction.generateUriMailTo());
    }
}
