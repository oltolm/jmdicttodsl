package jmdicttodsl;

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
    public void testWarodai() {
        String input = "あいちゃく【愛着･愛著】(айтяку)〔1-011-1-28〕\n" +
"привязанность, любовь;\n" +
"…に対して愛着を感じるようになる привязаться <i>к кому-л.</i>, полюбить <i>кого-л.</i>;\n" +
"彼は愛着の念に乏しい он человек холодный по натуре.\n";

        String expectedOutput = "あいちゃく\n" +
"愛着\n" +
"愛著\n" +
"	[m1][c maroon]あいちゃく[/c][c navy]【愛着･愛著】[/c][c red](айтяку)[/c][/m]\n" +
"	[trn][m2]привязанность, любовь;[/m]\n" +
"	[m4][ex][*]…に対して愛着を感じるようになる привязаться [i]к кому-л.[/i], полюбить [i]кого-л.[/i];[/*][/ex][/m]\n" +
"	[m4][ex][*]彼は愛着の念に乏しい он человек холодный по натуре.[/*][/ex][/m][/trn]\n\n";
        WarodaiToDslConverter warodai = new WarodaiToDslConverter("", "");
        String actualOutput = warodai.convert(input, Pattern.compile("ав\\."));
        assertEquals(expectedOutput, actualOutput);
    }
}
