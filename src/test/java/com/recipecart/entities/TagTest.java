/* (C)2023 */
package com.recipecart.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TagTest {

    @ParameterizedTest
    @MethodSource("stringProvider")
    void testConstructTag(String s) {
        Tag t1 = new Tag(s);
        assertNotNull(t1);
    }

    static Stream<String> stringProvider() {
        return Stream.of("", "abc", "Hello World", "8*H\n\t(#WRU*H(*QH#i238rh9");
    }
}
