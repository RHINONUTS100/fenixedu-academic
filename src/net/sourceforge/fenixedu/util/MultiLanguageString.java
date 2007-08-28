package net.sourceforge.fenixedu.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.Language;

public class MultiLanguageString implements Serializable, Comparable<MultiLanguageString> {

    private Map<Language, String> contentsMap;

    public MultiLanguageString() {
        this.contentsMap = new HashMap<Language, String>();
    }

    public MultiLanguageString(final String content) {
        this();
        setContent(content);
    }

    public MultiLanguageString(final Language language, final String content) {
        this();
        setContent(language, content);
    }

    public Collection<String> getAllContents() {
        return contentsMap.values();
    }

    public Collection<Language> getAllLanguages() {
        return contentsMap.keySet();
    }

    public boolean isRequestedLanguage() {
        Language userLanguage = LanguageUtils.getUserLanguage();
        return userLanguage != null && userLanguage.equals(getContentLanguage());
    }

    public Language getContentLanguage() {
        Language userLanguage = LanguageUtils.getUserLanguage();
        if (userLanguage != null && hasLanguage(userLanguage)) {
            return userLanguage;
        }

        Language systemLanguage = LanguageUtils.getSystemLanguage();
        if (systemLanguage != null && hasLanguage(systemLanguage)) {
            return systemLanguage;
        }

        return contentsMap.isEmpty() ? null : contentsMap.keySet().iterator().next();
    }

    public void setContent(String text) {
        final Language userLanguage = LanguageUtils.getUserLanguage();
        if (userLanguage != null) {
            setContent(userLanguage, text);
        }
        final Language systemLanguage = LanguageUtils.getSystemLanguage();
        if (userLanguage != systemLanguage && !hasLanguage(systemLanguage)) {
            setContent(systemLanguage, text);
        }
    }

    public String getContent() {
        return getContent(getContentLanguage());
    }

    public String getContent(Language language) {
        return contentsMap.get(language);
    }

    public void setContent(Language language, String content) {
        if (language == null) {
            throw new IllegalArgumentException("language cannot be null");
        }
        contentsMap.put(language, content == null ? "" : content);
    }

    public String removeContent(Language language) {
        return contentsMap.remove(language);
    }

    public String toUpperCase() {
        return hasContent() ? getContent().toUpperCase() : null;
    }

    public boolean hasContent() {
        //return getContent() != null;
        return !isEmpty();
    }

    public boolean hasContent(Language language) {
        return getContent(language) != null;
    }

    public boolean hasLanguage(Language language) {
        return contentsMap.containsKey(language);
    }

    public String exportAsString() {
        final StringBuilder result = new StringBuilder();
        for (final Entry<Language, String> entry : contentsMap.entrySet()) {
            final Language key = entry.getKey();
            final String value = entry.getValue();
            result.append(key);
            result.append(value.length());
            result.append(':');
            result.append(value);
        }
        return result.toString();
    }

    /**
     * @return true if this multi language string contains no languages
     */
    public boolean isEmpty() {
        //return this.getAllLanguages().isEmpty();
        return contentsMap.isEmpty();
    }

    public static MultiLanguageString importFromString(String string) {
        if (string == null) {
            return null;
        }

        MultiLanguageString mls = new MultiLanguageString();
        String nullContent = StringUtils.EMPTY;

        for (int i = 0; i < string.length();) {

            int length = 0;
            int collonPosition = string.indexOf(':', i + 2);

            if (!StringUtils.isNumeric(string.substring(i + 2, collonPosition))) {
                length = Integer.parseInt(string.substring(i + 4, collonPosition));
                nullContent = string.substring(collonPosition + 1, collonPosition + 1 + length);

            } else {
                length = Integer.parseInt(string.substring(i + 2, collonPosition));
                String language = string.substring(i, i + 2);
                String content = string.substring(collonPosition + 1, collonPosition + 1 + length);
                mls.setContent(Language.valueOf(language), content);
            }

            i = collonPosition + 1 + length;
        }

        // HACK: MultiLanguageString should not allow null values as language
        if (mls.getAllContents().isEmpty()) {
            mls.setContent(Language.getApplicationLanguage(), nullContent);
        }

        return mls;
    }

    @Override
    public String toString() {
        return getContent();
    }

    public int compareTo(MultiLanguageString languageString) {
        if (!hasContent() && !languageString.hasContent()) {
            return 0;
        }

        if (!hasContent() && languageString.hasContent()) {
            return -1;
        }

        if (hasContent() && !languageString.hasContent()) {
            return 1;
        }

        return getContent().compareTo(languageString.getContent());
    }

    public class I18N {
        public I18N add(String language, String text) {
            MultiLanguageString.this.setContent(Language.valueOf(language), text);
            return this;
        }

        public I18N nadd(String language, String text) {
            return text != null ? add(language, text) : this;
        }

        public MultiLanguageString finish() {
            return MultiLanguageString.this;
        }
    }

    public static I18N i18n() {
        return new MultiLanguageString().new I18N();
    }

}
