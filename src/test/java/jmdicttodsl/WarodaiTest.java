package jmdicttodsl;

import java.io.IOException;
import java.util.regex.Pattern;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Oleg Tolmatcev <oleg.tolmatcev@yahoo.de>
 */
public class WarodaiTest {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testWarodai() throws IOException {
        String input = "あげはのちょう, あげはちょう【揚羽蝶･鳳蝶】(агэханотё:, агэхатё:)〔1-005-1-56〕\n" +
"1) кавалер ксут, Papilio xuthus <i>L. (бабочка)</i>;\n" +
"2) <i>название герба с изображением этой бабочки.</i>\n" +
"• Также 【揚げ羽蝶】.\n";

        String expectedOutput = "あげはのちょう\n" +
"あげはちょう\n" +
"揚羽蝶\n" +
"鳳蝶\n" +
"	[m1][c maroon]あげはのちょう, あげはちょう[/c][c navy]【揚羽蝶･鳳蝶】[/c][c red](агэханотё:, агэхатё:)[/c][/m]\n" +
"	[trn][m2]1) кавалер ксут, Papilio xuthus [i]L. (бабочка)[/i];[/m]\n" +
"	[m2]2) [i]название герба с изображением этой бабочки.[/i][/m]\n" +
"	[m2][c brown]• Также 【揚げ羽蝶】.[/c][/m][/trn]\n\n";
        WarodaiToDslConverter warodai = new WarodaiToDslConverter(null, null);
        String actualOutput = warodai.convert(input, Pattern.compile("ав\\."));
        assertEquals(expectedOutput, actualOutput);
    }
}
