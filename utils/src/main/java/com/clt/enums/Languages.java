package com.clt.enums;

public enum Languages {

    C(103, "C(GCC 14.1.0)"),
    CPP(105, "C++(GCC 14.1.0)"),
    JAVA_17(91, "Java(OpenJDK 17.0.6)"),
    PYTHON_3(109, "Python(3.13.2)"),
    JAVASCRIPT(97, "JavaScript(Node.js 20.17.0)"),
    GO(106, "Go(1.22.0)"),
    TYPESCRIPT(101, "TypeScript(5.6.2)"),
    RUST(108, "Rust(1.85.0)"),
    PHP(98, "PHP(8.3.11)"),
    RUBY(72, "Ruby(2.7.0)"),
    SCALA(112, "Scala(3.4.2)"),
    KOTLIN(111, "Kotlin(2.1.10)");

    public final int code;
    public final String language;
    Languages(int code, String language) {
        this.code = code;
        this.language = language;
    }

    public static int getCode(Languages language) {
        return language.code;
    }

    public static String getLanguage(Languages language) {
        return language.language;
    }

    public static Languages getLanguageByCode(int code) {
        for (Languages language : Languages.values()) {
            if (language.code == code) {
                return language;
            }
        }
        return null;
    }

    public static Languages getCodeByLanguage(String language) {
        for (Languages languageEnum : Languages.values()) {
            if (languageEnum.language.equals(language)) {
                return languageEnum;
            }
        }
        return null;
    }
}
