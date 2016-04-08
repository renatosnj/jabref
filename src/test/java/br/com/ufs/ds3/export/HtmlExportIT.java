package br.com.ufs.ds3.export;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.MetaData;
import net.sf.jabref.exporter.ExportFormats;
import net.sf.jabref.exporter.IExportFormat;
import net.sf.jabref.model.database.BibDatabase;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.IdGenerator;

public class HtmlExportIT {

    @BeforeClass
    public static void setUpBeforeClass() {
        Globals.prefs = JabRefPreferences.getInstance();
        ExportFormats.initAllExports();
    }

    @Test
    public void testHtmlExport() throws Exception {
        IExportFormat htmlExportFormat = ExportFormats.getExportFormat("html");
        Set<String> entryIds = new HashSet<>();
        IdGenerator idGenerator = new IdGenerator();
        entryIds.add(idGenerator.next());
        entryIds.add(idGenerator.next());

        BibDatabase database = new BibDatabase();
        for (String entryId : entryIds) {
            BibEntry entry = new BibEntry(entryId);
            database.insertEntry(entry);
        }

        MetaData metaData = new MetaData();
        File targetFile = File.createTempFile("jabref", "test");
        htmlExportFormat.performExport(database, metaData, targetFile.toString(), StandardCharsets.UTF_8, entryIds);
        byte[] html = Files.readAllBytes(targetFile.toPath());
        Assert.assertTrue(html.length > 0);
    }

    @Test
    public void testHtmlExportWithoutEntries() throws Exception {
        IExportFormat htmlExportFormat = ExportFormats.getExportFormat("html");
        Set<String> entryIds = new HashSet<>();
        BibDatabase database = new BibDatabase();

        MetaData metaData = new MetaData();
        File targetFile = File.createTempFile("jabref", "test");
        htmlExportFormat.performExport(database, metaData, targetFile.toString(), StandardCharsets.UTF_8, entryIds);
        byte[] html = Files.readAllBytes(targetFile.toPath());
        Assert.assertTrue(html.length == 0);
    }
}
