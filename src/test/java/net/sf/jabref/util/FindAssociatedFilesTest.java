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

/**
 *  Este metodo depende uma opcao das preferencias, chamada
 *  AUTOLINK_EXACT_KEY_ONLY (Variavel booleanda). Ela define
 *  que ao tentar encontrar os arquivos automaticamente, o
 *  JabRef deve: associar arquivos que tenham os nomes
 *  exatamente identicos a BibTexKey da BibEntry (caso
 *  seja TRUE); ou associar arquivos que comecem com a
 *  BibTexKey da BibEntry.
 *
 *  Para se referir a esta distincao, os metodos foram
 *  nomeados da seguinte maneira:
 *  AUTOLINK_EXACT_KEY_ONLY = FALSE -> autolinkBuscaChaveNoInicio
 *  AUTOLINK_EXACT_KEY_ONLY = FALSE -> autolinkBuscaChaveExata
 *
 *  Logo apos exite o tipo de BibTexKey que esta sendo passada
 *  para teste:
 *  KeyExata -> existe um arquivo nomeado com a BibTexKey
 *  KeyNoInicio -> Existe um arquivo que seu nome comeca com
 *      a BibTexKey
 *  KeySemCorrespondencia -> nao existe um arquivo nomeado com a
 *       BibTexKey
 *  KeyVazia -> BibTexKey == ""
 *  KeyNula -> BibTexKey nula
 *  EntryNula -> Não apenas a BibTexKey e nula, mas toda a BibEntry
 */
public class FindAssociatedFilesTest {

    // diretorio de arquivos
    String s = File.separator;
    private final String filesDirectory = System.getProperty("user.dir") + s + "src" + s + "test" + s + "resources" + s
            + "net" + s + "sf" + s + "jabref" + s + "util" + s + "findAssociatedFiles";


    @BeforeClass
    public static void setUpBeforeClass() {
        Globals.prefs = JabRefPreferences.getInstance();
    }

    /**
     *  Exercita os caminhos quando AUTOLINK_EXACT_KEY_ONLY = FALSE
     *  Destinam-se a testar todas as condicoes do FOR interno
     *  da condicao:
     *  "if (!exactOnly)"
     */

    /**
     * Testa caminho quando a condicao (abaixo) e satisfeita:
     * if (name.startsWith(citeKey))
     */
    @Test
    public void autolinkBuscaChaveNoInicioKeyNoInicio() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.FALSE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTexKeyComecaCom());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertFalse(files.isEmpty());
        assertEquals(files.size(), 1);
        assertEquals(files.get(0).getName(), "comecaComABibTexKey.pdf");
    }

    /**
     * Testa caminho quando a condicao (abaixo) e satisfeita por
     * outro tipo de entrada:
     * if (name.startsWith(citeKey))
     */
    @Test
    public void autolinkBuscaChaveNoInicioKeyExata() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.FALSE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTexKeyNomeExato());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertFalse(files.isEmpty());
        assertEquals(files.size(), 1);
        assertEquals(files.get(0).getName(), "exatamenteABibTexKey.pdf");
    }

    /**
     * Testa caminho quando a condicao (abaixo) NAO e satisfeita:
     * if (name.startsWith(citeKey))
     */
    @Test
    public void autolinkBuscaChaveNoInicioKeySemCorrespondencia() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.FALSE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTexKeySemCorrespondencia());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     * Testa condicao !citeKey.isEmpty() do IF:
     * if ((citeKey != null) && !citeKey.isEmpty())
     *
     */
    @Test
    public void autolinkBuscaChaveNoInicioKeyVazia() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.FALSE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTextVazio());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     * Testa condicao citeKey != null do IF:
     * if ((citeKey != null) && !citeKey.isEmpty())
     *
     */
    @Test
    public void autolinkBuscaChaveNoInicioKeyNula() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.FALSE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTextCiteKeyNull());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     * Testa comportamento do metodo quando a entrada e nula
     */
    @Test
    public void autolinkBuscaChaveNoInicioEntryNula() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.FALSE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTextCiteKeyNull());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     *  Exercita os caminhos quando AUTOLINK_EXACT_KEY_ONLY = TRUE
     *  Destina-se a testar as condicoes do primeiro for da interacao:
     *  nextFile: for (File file : filesWithExtension)
     */

    /**
     * Testa caminho quando a condicao (abaixo) e satisfeita:
     * if (name.substring(0, dot).equals(citeKey))
     */
    @Test
    public void autolinkBuscaChaveExataKeyNoInicio() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.TRUE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTexKeyComecaCom());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     * Testa caminho quando a condicao (abaixo) NAO e satisfeita:
     * if (name.substring(0, dot).equals(citeKey))
     */
    @Test
    public void autolinkBuscaChaveExataKeyExata() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.TRUE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTexKeyNomeExato());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertFalse(files.isEmpty());
        assertEquals(files.size(), 1);
        assertEquals(files.get(0).getName(), "exatamenteABibTexKey.pdf");
    }


    /**
     * Testa caminho quando a condicao (abaixo) NAO e satisfeita, por
     * nao haver arquivo correspondente a BibEntry:
     * if (name.substring(0, dot).equals(citeKey))
     */
    @Test
    public void autolinkBuscaChaveExataKeySemCorrespondencia() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.TRUE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTexKeySemCorrespondencia());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     * Testa condicao !citeKey.isEmpty() do IF:
     * if ((citeKey != null) && !citeKey.isEmpty())
     *
     */
    @Test
    public void autolinkBuscaChaveExataKeyVazia() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.TRUE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTextVazio());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     * Testa condicao citeKey != null do IF:
     * if ((citeKey != null) && !citeKey.isEmpty())
     *
     */
    @Test
    public void autolinkBuscaChaveExataKeyNula() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.TRUE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTextCiteKeyNull());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     * Testa comportamento do metodo quando a chave e nula
     */
    @Test
    public void autolinkBuscaChaveExataEntryNula() {
        Globals.prefs.putBoolean(JabRefPreferences.AUTOLINK_EXACT_KEY_ONLY, Boolean.TRUE);

        Map<BibEntry, List<File>> retorno = executandoMetodo(BibTexNull());

        Entry<BibEntry, List<File>> entryFilePair = retorno.entrySet().iterator().next();
        List<File> files = entryFilePair.getValue();
        assertTrue(files.isEmpty());
    }

    /**
     * Executa  findAssociatedFiles e retorna a associacao
     *
     * @param Collection<BibEntry> entries Entrada que sera utilizada para
     *      localizar arquivo.
     */
    private Map<BibEntry, List<File>> executandoMetodo(Collection<BibEntry> entries) {
        Collection<String> extensions = new ArrayList<>();
        extensions.add("pdf");

        // Criando uma lista com o diretorio de arquivos
        List<File> directories = Arrays.asList(new File(filesDirectory));

        Map<BibEntry, List<File>> retorno;
        retorno = Util.findAssociatedFiles(entries, extensions, directories);
        return retorno;
    }

    /**
     *  Gerando tres casos de teste das BibEntry
     */

    private Collection<BibEntry> BibTexNull() {
        Collection<BibEntry> entries = new ArrayList<>();
        BibEntry bibTexKeyNull = null;
        entries.add(bibTexKeyNull);
        return entries;
    }

    private Collection<BibEntry> BibTextCiteKeyNull() {
        Collection<BibEntry> entries = new ArrayList<>();
        BibEntry semBibTexKey = new BibEntry("000000003", BibtexEntryTypes.ARTICLE);
        entries.add(semBibTexKey);
        return entries;
    }

    private Collection<BibEntry> BibTextVazio() {
        Collection<BibEntry> entries = new ArrayList<>();
        BibEntry semBibTexKey = new BibEntry("000000003", BibtexEntryTypes.ARTICLE);
        semBibTexKey.setField("bibtexkey", "");
        entries.add(semBibTexKey);
        return entries;
    }

    private Collection<BibEntry> BibTexKeySemCorrespondencia() {
        Collection<BibEntry> entries = new ArrayList<>();
        BibEntry semCorrespondencia = new BibEntry("000000003", BibtexEntryTypes.ARTICLE);
        semCorrespondencia.setField("bibtexkey", "semCorrespondencia");
        entries.add(semCorrespondencia);
        return entries;
    }

    private Collection<BibEntry> BibTexKeyNomeExato() {
        Collection<BibEntry> entries = new ArrayList<>();
        BibEntry corespondeciaExata = new BibEntry("000000002", BibtexEntryTypes.ARTICLE);
        corespondeciaExata.setField("bibtexkey", "exatamenteABibTexKey");
        entries.add(corespondeciaExata);
        return entries;
    }

    private Collection<BibEntry> BibTexKeyComecaCom() {
        Collection<BibEntry> entries = new ArrayList<>();
        BibEntry correspondeciaInicio = new BibEntry("00000001", BibtexEntryTypes.ARTICLE);
        correspondeciaInicio.setField("bibtexkey", "comecaCom");
        entries.add(correspondeciaInicio);
        return entries;
    }

}
