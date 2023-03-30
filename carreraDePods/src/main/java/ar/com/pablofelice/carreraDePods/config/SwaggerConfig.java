/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ar.com.pablofelice.carreraDePods.config;

/**
 *
 * @author Usuario
 */
public class SwaggerConfig {
    public static final String ejemploBodyPodhealth = "{\n" +
            "  \"antenas\": [\n" +
            "    {\n" +
            "      \"name\": \"antena0\",\n" +
            "      \"pod\": \"Anakin Skywalker\",\n" +
            "      \"distance\": 210.0,\n" +
            "      \"metrics\": [\n" +
            "        \"590C\",\n" +
            "        \"\",\n" +
            "        \"\",\n" +
            "        \"60%\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"antena1\",\n" +
            "      \"pod\": \"Anakin Skywalker\",\n" +
            "      \"distance\": 225.5,\n" +
            "      \"metrics\": [\n" +
            "        \"\",\n" +
            "        \"1MWh\",\n" +
            "        \"\",\n" +
            "        \"60%\"\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"antena2\",\n" +
            "      \"pod\": \"Anakin Skywalker\",\n" +
            "      \"distance\": 252.7,\n" +
            "      \"metrics\": [\n" +
            "        \"590C\",\n" +
            "        \"\",\n" +
            "        \"110C, \",\n" +
            "        \"\"\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    public static final String ejemploBodyPodhealthSplit = "{\n" +
            "  \"pod\": \"Anakin Skywalker\",\n" +
            "  \"distance\": 100.0,\n" +
            "  \"message\": [\"590C\", \"1MWh\", \"110C\", \"60%\"]\n" +
            "}";
}
    

