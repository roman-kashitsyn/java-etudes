package etudes.wget;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ProgressBar}.
 * @author <a href="mailto:roman.kashitsyn@gmail.com">Roman Kashitsyn</a>
 */
public class ProgressBarTest {

    @Test
    public void test10WidthProgressBar() {
        ProgressBar progressBar = ProgressBar.ofWidth(12);
        assertEquals("[=>        ]", progressBar.render(20));
        assertEquals("[==>       ]", progressBar.render(25));
        assertEquals("[====>     ]", progressBar.render(50));
        assertEquals("[=======>  ]", progressBar.render(75));
        assertEquals("[==========]", progressBar.render(100));
    }
}
