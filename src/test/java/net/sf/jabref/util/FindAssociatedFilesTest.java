package net.sf.jabref.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.BibtexEntryTypes;

public class FindAssociatedFilesTest {

    // diretorio de arquivos
    private final String filesDirectory = "/src/test/resources/net/sf/jabref/util/findAssociatedFiles";
    private final String filesDirectory2 = "D:/AppData/dev/git/jabref/src/test/resources/net/sf/jabref/util/findAssociatedFiles";


    @BeforeClass
    public static void setUpBeforeClass() {
        Globals.prefs = JabRefPreferences.getInstance();
    }

    /**
     *  Exercita os caminhos ativos quando a preferencia
     *  de arquivo ter o nome comecando com a BibTexKey
     *  esta habilitada
     */
    @Test
    public void testFindAssociatedFilesInitBibkey() {

        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.FALSE);

        Map<BibEntry, List<File>> retorno = executandoMetodo();

        for (Entry<BibEntry, List<File>> entryFilePair : retorno.entrySet()) {
            BibEntry entrada = entryFilePair.getKey();
            List<File> files = entryFilePair.getValue();
            if (entrada.getCiteKey().equals("comecaComABibTexKey")) {
                // Certifica-se que os que o aquivo que comeca com a
                // BibTexKey foi associado corretamente
                assertFalse(files.isEmpty());
                assertEquals(files.size(), 1);
                assertEquals(files.get(0).getName(), "comecaComABibTexKey.pdf");
            } else if (entrada.getCiteKey().equals("exatamenteABibTexKey")) {
                // Certifica-se que os arquivos que tem exatamente a
                // BibTexKey como nome foram associados
                assertFalse(files.isEmpty());
                assertEquals(files.size(), 1);
                assertEquals(files.get(0).getName(), "exatamenteABibTexKey.pdf");
            } else if (entrada.getCiteKey().equals("semCorrespondencia")) {
                // Certifica-se que Entradas sem arquivos com nomes
                // correspondentes com a BibTexKey continuam
                // sem associacao
                assertTrue(files.isEmpty());
            } else if (entrada.getCiteKey().equals("")) {
                // Certifica-se que Entradas sem BibTexKey continuam
                // sem associacao
                assertTrue(files.isEmpty());
            }
        }
    }

    /**
     *  Exercita os caminhos ativos quando a preferencia
     *  de arquivo ter o nome exatamente igual a BibTexKey
     *  esta habilitada
     */
    @Test
    public void testFindAssociatedFilesExactlyBibkey() {

        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.TRUE);

        Map<BibEntry, List<File>> retorno = executandoMetodo();

        for (Entry<BibEntry, List<File>> entryFilePair : retorno.entrySet()) {
            BibEntry entrada = entryFilePair.getKey();
            List<File> files = entryFilePair.getValue();
            if (entrada.getCiteKey().equals("comecaCom")) {
                // Certifica-se que os arquivos que nao tem
                // a BibTexKey como seu nome, nao foram associados
                assertTrue(files.isEmpty());
            } else if (entrada.getCiteKey().equals("exatamenteABibTexKey")) {
                // Certifica-se que os arquivos que tem exatamente a
                // BibTexKey como nome foram associados
                assertFalse(files.isEmpty());
                assertEquals(files.size(), 1);
                assertEquals(files.get(0).getName(), "exatamenteABibTexKey.pdf");
            } else if (entrada.getCiteKey().equals("semCorrespondencia")) {
                // Certifica-se que Entradas sem arquivos com nomes
                // correspondentes com a BibTexKey continuam
                // sem associacao
                assertTrue(files.isEmpty());
            } else if (entrada.getCiteKey().equals("")) {
                // Certifica-se que Entradas sem BibTexKey continuam
                // sem associacao
                assertTrue(files.isEmpty());
            }
        }
    }

    /**
     * Executa  findAssociatedFiles e retorna a associacao
     * OBS.: Achei necessario refatorar, pois uso nos dois casos de teste
     */
    private Map<BibEntry, List<File>> executandoMetodo() {
        Collection<String> extensions = new ArrayList<>();
        extensions.add("pdf");

        // Criando uma lista com o diretorio de arquivos
        List<File> directories = Arrays.asList(new File(filesDirectory2));

        // Simulando entradas
        Collection<BibEntry> entries = gerarEntradas();

        Map<BibEntry, List<File>> retorno;
        retorno = Util.findAssociatedFiles(entries, extensions, directories);
        return retorno;
    }

    /**
     *  Gerando tres casos de entradas para testar os caminhos:
     *      (1) Localizacao de arquivos que tem o nome que comeca com a BibTexKey
     *      (2) Localizacao de arquivos que tem o nome exatamente igual a BibTexKey
     *      (3) BibTexKey sem correspondencia de arquivo
     *      (4) BibEntry sem BibTexKey
     */
    private Collection<BibEntry> gerarEntradas() {
        Collection<BibEntry> entries = new ArrayList<>();
        BibEntry correspondeciaInicio = new BibEntry("00000001", BibtexEntryTypes.ARTICLE);
        correspondeciaInicio.setField("bibtexkey", "comecaCom");
        BibEntry corespondeciaExata = new BibEntry("000000002", BibtexEntryTypes.ARTICLE);
        corespondeciaExata.setField("bibtexkey", "exatamenteABibTexKey");
        BibEntry semCorrespondencia = new BibEntry("000000003", BibtexEntryTypes.ARTICLE);
        semCorrespondencia.setField("bibtexkey", "semCorrespondencia");
        BibEntry semBibTexKey = new BibEntry("000000003", BibtexEntryTypes.ARTICLE);
        semBibTexKey.setField("bibtexkey", "");
        entries.add(semCorrespondencia);
        entries.add(corespondeciaExata);
        entries.add(correspondeciaInicio);
        entries.add(semBibTexKey);
        return entries;
    }

}
