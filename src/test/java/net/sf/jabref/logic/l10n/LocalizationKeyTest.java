package net.sf.jabref.logic.l10n;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocalizationKeyTest {

    @Test
    public void testConversionToPropertiesKey() {
        LocalizationKey localizationKey = new LocalizationKey("test : =");
        assertEquals("test_\\:_\\=", localizationKey.getPropertiesKey());
        assertEquals("test_:_=", localizationKey.getPropertiesKeyUnescaped());
        assertEquals("test : =", localizationKey.getTranslationValue());
    }

}