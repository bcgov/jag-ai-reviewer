package ca.bc.gov.open.jag.aidiligenclient.diligen.processor;

import ca.bc.gov.open.jag.aidiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.aidiligenclient.api.model.Field;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FieldProcessorTest {

    private FieldProcessorImpl sut;

    @BeforeAll
    public void beforeAll() {
        sut = new FieldProcessorImpl();
    }

    @Test
    public void testProduct() throws IOException, JSONException {

        Path path = Paths.get("src/test/resources/formData.json");

        Path path2 = Paths.get("src/test/resources/diligen.answer.1.json");

        Path path3 = Paths.get("src/test/resources/details.json");

        ObjectMapper mapper = new ObjectMapper();


        DocumentConfig formData = mapper.readValue(new String(Files.readAllBytes(path)), DocumentConfig.class);
        //Different structure require a different naming strategy
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        List<Field> response = mapper.readValue(new String(
                Files.readAllBytes(path2)), new TypeReference<List<Field>>(){});


        ObjectNode actual = sut.getJson(formData, response, new JSONObject(new String(Files.readAllBytes(path3))));

        Assertions.assertEquals(6, actual.size());

        Assertions.assertEquals("\"ERRORCODES,ERRORCODESCODES,ERRORCODESCODESCODES\"", actual.get("errorCodes").toString());

        Assertions.assertEquals("\"2057551\"", actual.get("test").toString());

        Assertions.assertEquals("\"2057551\"", actual.get("test2").get("test").toString());

        Assertions.assertTrue(actual.get("test3").get("test").isArray());

        Assertions.assertTrue(actual.get("test4").get("test").isArray());

        Assertions.assertTrue(actual.get("test5").get("test").isArray());

    }

}
