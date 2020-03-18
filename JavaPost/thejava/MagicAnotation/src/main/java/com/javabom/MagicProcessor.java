package com.javabom;

import com.google.auto.service.AutoService;
import com.javabom.annotation.Magic;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AutoService(Processor.class)
public class MagicProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return new HashSet<>(Arrays.asList(Magic.class.getName()));
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //적절한 곳에 어노테이션이 붙어 있는지 확인
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
