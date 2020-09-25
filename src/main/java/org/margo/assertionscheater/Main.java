package org.margo.assertionscheater;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;
import org.margo.assertionscheater.Objects.Address;
import org.margo.assertionscheater.Objects.Person;

public class Main {

  public static final String ACTUAL = "actual";

  public static void main(String[] args) {
    String destinationPackage = "org.margo.assertionscheater.assertions";
    generateAssertClass(Person.class, destinationPackage);
    generateAssertClass(Address.class, destinationPackage);
  }

  private static <T> void generateAssertClass(Class<T> clazz, String destinationPackage) {
    String assertClassName = clazz.getSimpleName() + "Assert";

    MethodSpec constructor = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PRIVATE)
      .addParameter(clazz, ACTUAL)
      .addStatement("this.actual = actual")
      .build();

    MethodSpec assertThatMethod = MethodSpec.methodBuilder("assertThat")
      .returns(ClassName.get(destinationPackage, assertClassName))
      .addParameter(clazz, ACTUAL)
      .addStatement("return new " + assertClassName + "(actual)")
      .build();

    FieldSpec actualField = FieldSpec.builder(clazz, ACTUAL)
      .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
      .build();

    List<MethodSpec> fieldAssertionMethods = new ArrayList<>();

    for (Field field: clazz.getDeclaredFields()) {
      String fieldName = field.getName();
      String errorMessage = String.format("\"Unexpected %s. Expected: \" + %s + \" but was: \" + actual.get%s() + \" .\"", fieldName, fieldName, capitalize(fieldName));
      MethodSpec method = MethodSpec.methodBuilder("has" + capitalize(fieldName))
        .returns(ClassName.get(destinationPackage, assertClassName))
        .addModifiers(Modifier.PUBLIC)
        .addParameter(field.getType(), fieldName)
        .beginControlFlow(equalsCondition(field))
        .addStatement("fail(" + errorMessage + ")")
        .endControlFlow()
        .addStatement("return this")
        .build();

      fieldAssertionMethods.add(method);
    }

    TypeSpec clazzAssert = TypeSpec.classBuilder(assertClassName)
      .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
      .addField(actualField)
      .addMethod(constructor)
      .addMethod(assertThatMethod)
      .addMethods(fieldAssertionMethods)
      .build();

    JavaFile javaFile = JavaFile.builder(destinationPackage, clazzAssert)
      .addStaticImport(org.assertj.core.api.Assertions.class, "fail")
      .build();

    try {
      javaFile.writeTo(new File("generated"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private static String capitalize(String str) {
    return str.length() > 0 ?
      str.substring(0, 1).toUpperCase() + str.substring(1) :
      str.toUpperCase();
  }

  private static String equalsCondition(Field field) {
    return  field.getType().isPrimitive()?
      String.format("if(!(actual.get%s() == %s))", capitalize(field.getName()), field.getName()):
      String.format("if(!(actual.get%s().equals(%s)))", capitalize(field.getName()), field.getName());
  }

}
