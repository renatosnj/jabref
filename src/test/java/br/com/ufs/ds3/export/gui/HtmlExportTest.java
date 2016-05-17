package br.com.ufs.ds3.export.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFrame;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.assertj.swing.launcher.ApplicationLauncher;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.jabref.JabRefMain;
import net.sf.jabref.exporter.ExportFormats;
import net.sf.jabref.exporter.IExportFormat;

public class HtmlExportTest extends AssertJSwingJUnitTestCase {

    private FrameFixture frameFixture;

    @BeforeClass
    public static void before() {
        FailOnThreadViolationRepaintManager.uninstall();
    }

    @Override
    protected void onSetUp() {
        ApplicationLauncher.application(JabRefMain.class).start();
        frameFixture = WindowFinder.findFrame(JFrame.class).using(robot());
    }

    @Override
    protected void onTearDown() {
        frameFixture.close();
        frameFixture = null;
    }

    @Test
    public void exportEntriesToHtml() throws IOException {
        frameFixture.menuItemWithPath("File", "Export").click();
        JFileChooserFixture fileChooser = frameFixture.fileChooser();
        File targetFile = File.createTempFile("jabref", "test.html");
        fileChooser.setCurrentDirectory(targetFile.getParentFile());
        fileChooser.fileNameTextBox().setText(targetFile.getName());
        IExportFormat htmlExportFormat = ExportFormats.getExportFormat("html");
        fileChooser.target().setFileFilter(htmlExportFormat.getFileFilter());
        fileChooser.approve();
        frameFixture.optionPane().okButton().click();
        byte[] html = Files.readAllBytes(targetFile.toPath());
        Assert.assertTrue(html.length > 0);
    }
}
