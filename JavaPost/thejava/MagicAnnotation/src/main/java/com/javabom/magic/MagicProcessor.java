package com.javabom.magic;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class MagicProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> names = new HashSet<>();
        names.add(Magic.class.getName());
        return names;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return super.getSupportedSourceVersion();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //내가 원하는 곳에 어노테이션이 붙어 있는지 확인
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Magic.class);
        for (Element element : elementsAnnotatedWith) {
            Name elementSimpleName = element.getSimpleName();
            if (element.getKind() != ElementKind.INTERFACE) {
                processingEnv.getMessager()
                        .printMessage(Diagnostic.Kind.ERROR, String.format("%s doesn't support %s", Magic.class.getName(), elementSimpleName));
            } else {
                processingEnv.getMessager()
                        .printMessage(Diagnostic.Kind.NOTE, String.format("Processing %s", elementSimpleName));
            }
        }
        return true;
    }
}
