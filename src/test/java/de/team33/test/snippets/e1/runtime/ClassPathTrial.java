package de.team33.test.snippets.e1.runtime;

import de.team33.snippets.e1.runtime.ClassPath;

final class ClassPathTrial {

    private ClassPathTrial() {
    }

    public static void main(final String[] args) {
        //ClassPath.urls().forEach(System.out::println);
        ClassPath.classNames()
                 .filter(name -> name.startsWith("de.team33.")).forEach(System.out::println);
    }
}