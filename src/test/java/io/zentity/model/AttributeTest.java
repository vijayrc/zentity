package io.zentity.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.zentity.common.Json;
import io.zentity.resolution.input.value.Value;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AttributeTest {

    public final static String VALID_OBJECT = "{\"type\":\"string\"}";

    ////  "attributes"  ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testValid() throws Exception {
        new Attribute("attribute_name", VALID_OBJECT);
        new Attribute("attribute_name", "{}");
    }

    @Test(expected = ValidationException.class)
    public void testInvalidUnexpectedField() throws Exception {
        new Attribute("attribute_name", "{\"type\":\"string\",\"foo\":\"bar\"}");
    }

    ////  "attributes".ATTRIBUTE_NAME  /////////////////////////////////////////////////////////////////////////////////

    @Test(expected = ValidationException.class)
    public void testInvalidNameEmpty() throws Exception {
        new Attribute(" ", VALID_OBJECT);
    }

    ////  "attributes".ATTRIBUTE_NAME."type"  //////////////////////////////////////////////////////////////////////////

    @Test
    public void testValidTypeValue() throws Exception {
        for (String value : Attribute.VALID_TYPES)
            new Attribute("attribute_name", "{\"type\":\"" + value + "\"}");
    }

    @Test(expected = ValidationException.class)
    public void testInvalidTypeValue() throws Exception {
        new Attribute("attribute_name", "{\"type\":\"foobar\"}");
    }

    @Test(expected = ValidationException.class)
    public void testInvalidTypeTypeArray() throws Exception {
        new Attribute("attribute_name", "{\"type\":[]}");
    }

    @Test(expected = ValidationException.class)
    public void testInvalidTypeTypeBoolean() throws Exception {
        new Attribute("attribute_name", "{\"type\":true}");
    }

    @Test(expected = ValidationException.class)
    public void testInvalidTypeTypeFloat() throws Exception {
        new Attribute("attribute_name", "{\"type\":1.0}");
    }

    @Test(expected = ValidationException.class)
    public void testInvalidTypeTypeInteger() throws Exception {
        new Attribute("attribute_name", "{\"type\":1}");
    }

    @Test(expected = ValidationException.class)
    public void testInvalidTypeTypeNull() throws Exception {
        new Attribute("attribute_name", "{\"type\":null}");
    }

    @Test(expected = ValidationException.class)
    public void testInvalidTypeTypeObject() throws Exception {
        new Attribute("attribute_name", "{\"type\":{}}");
    }

    ////  Input Data Type Detection  ///////////////////////////////////////////////////////////////////////////////////

    private JsonNode jsonValue(String json) throws IOException {
        return Json.MAPPER.readTree(json).get("value");
    }

    @Test
    public void testValidIsTypeBooleanFalse() throws Exception {
        Value.create("boolean", jsonValue("{\"value\":false}"));
    }

    @Test
    public void testValidIsTypeBooleanTrue() throws Exception {
        Value.create("boolean", jsonValue("{\"value\":true}"));
    }

    @Test
    public void testValidIsTypeNumberIntegerLongNegative() throws Exception {
        Value.create("number", jsonValue("{\"value\":-922337203685477}"));
    }

    @Test
    public void testValidIsTypeNumberIntegerLongPositive() throws Exception {
        Value.create("number", jsonValue("{\"value\":922337203685477}"));
    }

    @Test
    public void testValidIsTypeNumberIntegerShortNegative() throws Exception {
        Value.create("number", jsonValue("{\"value\":-1}"));
    }

    @Test
    public void testValidIsTypeNumberIntegerShortPositive() throws Exception {
        Value.create("number", jsonValue("{\"value\":1}"));
    }

    @Test
    public void testValidIsTypeNumberFloatLongNegative() throws Exception {
        Value.create("number", jsonValue("{\"value\":-3.141592653589793}"));
    }

    @Test
    public void testValidIsTypeNumberFloatLongPositive() throws Exception {
        Value.create("number", jsonValue("{\"value\":3.141592653589793}"));
    }

    @Test
    public void testValidIsTypeNumberFloatShortNegative() throws Exception {
        Value.create("number", jsonValue("{\"value\":-1.0}"));
    }

    @Test
    public void testValidIsTypeNumberFloatShortPositive() throws Exception {
        Value.create("number", jsonValue("{\"value\":1.0}"));
    }

    @Test
    public void testValidIsTypeString() throws Exception {
        Value.create("string", jsonValue("{\"value\":\"a\"}"));
    }

    ////  Booleans must not be strings or numbers

    @Test(expected = ValidationException.class)
    public void testInvalidIsTypeBooleanFalseInteger() throws Exception {
        Value.create("boolean", jsonValue("{\"value\":0}"));
    }

    @Test(expected = ValidationException.class)
    public void testInvalidIsTypeBooleanFalseString() throws Exception {
        Value.create("boolean", jsonValue("{\"value\":\"false\"}"));
    }

    @Test(expected = ValidationException.class)
    public void testInvalidIsTypeBooleanTrueInteger() throws Exception {
        Value.create("boolean", jsonValue("{\"value\":1}"));
    }

    @Test(expected = ValidationException.class)
    public void testInvalidIsTypeBooleanTrueString() throws Exception {
        Value.create("boolean", jsonValue("{\"value\":\"true\"}"));
    }

    ////  Validate Nullable Data Types  ////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testValidValidateTypeBooleanNullable() throws Exception {
        Value.create("boolean", jsonValue("{\"value\":null}"));
    }

    @Test
    public void testValidValidateTypeNumberNullable() throws Exception {
        Value.create("number", jsonValue("{\"value\":null}"));
    }

    @Test
    public void testValidValidateTypeStringNullable() throws Exception {
        Value.create("string", jsonValue("{\"value\":null}"));
    }

    ////  Input Data Type Conversion  //////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testValidSerializeTypeBooleanFalse() throws Exception {
        Assert.assertEquals(Value.create("boolean", jsonValue("{\"value\":false}")).serialized(), "false");
    }

    @Test
    public void testValidSerializeTypeBooleanTrue() throws Exception {
        Assert.assertEquals(Value.create("boolean", jsonValue("{\"value\":true}")).serialized(), "true");
    }

    @Test
    public void testValidSerializeTypeNumberIntegerLongNegative() throws Exception {
        Assert.assertEquals(Value.create("number", jsonValue("{\"value\":-922337203685477}")).serialized(), "-922337203685477");
    }

    @Test
    public void testValidSerializeTypeNumberIntegerLongPositive() throws Exception {
        Assert.assertEquals(Value.create("number", jsonValue("{\"value\":922337203685477}")).serialized(), "922337203685477");
    }

    @Test
    public void testValidSerializeTypeNumberIntegerShortNegative() throws Exception {
        Assert.assertEquals(Value.create("number", jsonValue("{\"value\":-1}")).serialized(), "-1");
    }

    @Test
    public void testValidSerializeTypeNumberIntegerShortPositive() throws Exception {
        Assert.assertEquals(Value.create("number", jsonValue("{\"value\":1}")).serialized(), "1");
    }

    @Test
    public void testValidSerializeTypeNumberFloatLongNegative() throws Exception {
        Assert.assertEquals(Value.create("number", jsonValue("{\"value\":-3.141592653589793}")).serialized(), "-3.141592653589793");
    }

    @Test
    public void testValidSerializeTypeNumberFloatLongPositive() throws Exception {
        Assert.assertEquals(Value.create("number", jsonValue("{\"value\":3.141592653589793}")).serialized(), "3.141592653589793");
    }

    @Test
    public void testValidSerializeTypeNumberFloatShortNegative() throws Exception {
        Assert.assertEquals(Value.create("number", jsonValue("{\"value\":-1.0}")).serialized(), "-1.0");
    }

    @Test
    public void testValidSerializeTypeNumberFloatShortPositive() throws Exception {
        Assert.assertEquals(Value.create("number", jsonValue("{\"value\":1.0}")).serialized(), "1.0");
    }

    @Test
    public void testValidSerializeTypeString() throws Exception {
        Assert.assertEquals(Value.create("string", jsonValue("{\"value\":\"a\"}")).serialized(), "a");
    }

    ////  Nullable Input Data Type Conversion  /////////////////////////////////////////////////////////////////////////

    @Test
    public void testValidSerializeTypeBooleanNullable() throws Exception {
        Value value = Value.create("boolean", jsonValue("{\"value\":null}"));
        Assert.assertEquals(value.value(), null);
        Assert.assertEquals(value.serialized(), "null");
    }

    @Test
    public void testValidSerializeTypeNumberNullable() throws Exception {
        Value value = Value.create("number", jsonValue("{\"value\":null}"));
        Assert.assertEquals(value.value(), null);
        Assert.assertEquals(value.serialized(), "null");
    }

    @Test
    public void testValidSerializeTypeStringNullable() throws Exception {
        Value value = Value.create("string", jsonValue("{\"value\":null}"));
        Assert.assertEquals(value.value(), null);
        Assert.assertEquals(value.serialized(), "null");
    }
}
